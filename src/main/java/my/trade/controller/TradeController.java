package my.trade.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import my.comment.service.CommentService;
import my.comment.vo.CommentVO;
import my.notification.service.NotiSubscriptionService;
import my.notification.service.NotificationService;
import my.notification.vo.NotiSubscriptionVO;
import my.notification.vo.NotificationVO;
import my.notification.vo.SubscriptionVO;
import my.notification.vo.SubscriptionVO.Keys;
import my.trade.service.TradeService;
import my.trade.vo.PageVO;
import my.trade.vo.TradeVO;
import my.user.service.UserKeywordService;
import my.user.vo.UserKeywordVO;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;


@Controller
public class TradeController {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
	private static final int MAX_SIZE = 1 * 1024 * 1024; // 5MB
    //private static final int MAX_WIDTH = 800;  // Adjust if needed
    //private static final int MAX_HEIGHT = 800; // Adjust if needed
    private static final double SHRINK_FACTOR = 0.9; // Shrink by 10% each iteration

	@Autowired
	TradeService tradeService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
    SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	UserKeywordService userkeywordService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	NotiSubscriptionService notisubscriptionService;
	
	@RequestMapping(value="/tradeInsertPage.do")//게시글 등록 페이지 이동
	public String tradeInsertPage() {
		return "trade/tradeInsert.jsp";
	}
	
	@RequestMapping(value="/tradeListPage.do", method = RequestMethod.GET)
	public ModelAndView tradeListPage(@RequestParam(defaultValue = "1") int currentPage, 
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(required = false) String searchTerm) {
		ModelAndView mav = new ModelAndView("trade/tradeList.jsp");
		
		PageVO pagination = new PageVO();
        pagination.setCurrentPage(currentPage);
        pagination.setPageSize(pageSize);
        pagination.setSearchTerm(searchTerm);
        
        int totalPosts;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            totalPosts = tradeService.countPosts();
        } else {
            totalPosts = tradeService.countPostsBySearchTerm(searchTerm);
        }
        pagination.setTotalPosts(totalPosts);
        pagination.calculatePages();
        
		/*
		 * int startPage = (currentPage - 1) * pageSize;
		 * pagination.setStartPage(startPage); pagination.setEndPage(Math.min(startPage
		 * + pageSize, totalPosts));
		 */
		
		List<TradeVO> tradeList = tradeService.selectTradeList(pagination);
		
		mav.addObject("tradeList", tradeList);
		mav.addObject("pagination", pagination);
		
		return mav;
	}
	
	@RequestMapping(value="/findnotiRead.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView findnotiRead(@RequestParam String userId) {
		ModelAndView mav = new ModelAndView("jsonView");
		
		List<NotificationVO> notification = notificationService.findNotiIsRead(userId);
		
		if(notification.size() != 0) {
			mav.addObject("message", true);
		}
		else {
			mav.addObject("message", false);
		}
		
		return mav;
	}
	
	@RequestMapping(value="/tradeInsert.do", method = RequestMethod.POST)//게시글 등록 처리
	public String tradeInsert(@ModelAttribute TradeVO trade) throws IOException {
		// 파일 업로드 처리
		String image=null;
		MultipartFile uploadImage = trade.getUploadImage();
		
		if (!uploadImage.isEmpty()) {
			String originalFileName = uploadImage.getOriginalFilename();
			UUID uuid = UUID.randomUUID();	
			image=uuid+"_"+originalFileName;	
			
			Path outputPath = Paths.get("E:\\uploads\\" + image);
            
            // Check if file size exceeds the limit
            if (uploadImage.getSize() > MAX_SIZE) {
                // Resize image
                try (InputStream in = uploadImage.getInputStream()) {
                	BufferedImage originalImage = ImageIO.read(in);
                    int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
                    BufferedImage resizedImage = originalImage;
                    long length;
                    do {
                        resizedImage = resizeImage(resizedImage, type);
                        ImageIO.write(resizedImage, "jpg", outputPath.toFile()); // You may need to change the format depending on your needs
                        length = Files.size(outputPath);
                    } while (length > MAX_SIZE);
                }
            } else {
                uploadImage.transferTo(outputPath.toFile());
            }
		}
		trade.setImage(image);
		
		tradeService.insertTrade(trade);
		int tradeInsertId = trade.getTradeId();
		
		
		List<String> usersToNotify = new ArrayList<>();
		List<UserKeywordVO>userkeyword = userkeywordService.findTitleKeyword();
		
		for (UserKeywordVO Keyword : userkeyword) {
	        if (trade.getTitle().contains(Keyword.getKeyword())) {
	        	if(Keyword.getUserId() != trade.getWriterId())
	        		usersToNotify.add(Keyword.getUserId());
	        }
	    }
		
		Set<String> uniqueUsersToNotify = new HashSet<>(usersToNotify);
		
		int noticount = uniqueUsersToNotify.size();
		trade.setUserCount(noticount);
		
		for(String userId : uniqueUsersToNotify) {
			NotificationVO noti = new NotificationVO();
			noti.setUserId(userId);
			noti.setTradeId(tradeInsertId);
			noti.setUserCount(noticount);
			noti.setNotiStatus("IN");
			notificationService.insertNotification(noti);
			simpMessagingTemplate.convertAndSend("/topic/notifications/" + userId, "메시지가 있습니다.");
			List<NotiSubscriptionVO> notisub = notisubscriptionService.findUserNotiSub(userId);
			if(notisub.size() != 0) {
				for(NotiSubscriptionVO sub : notisub) {
					System.out.println("sendWebPush 호출 전");
					sendWebPush(sub, trade, userId);
					System.out.println("sendWebPush 호출 후");
				}
			}
        }
		
		return "redirect:/tradeListPage.do";
	}
	public void sendWebPush(NotiSubscriptionVO subscription, TradeVO trade, String userId) {
		System.out.println("push 블록 시작");
		//Security.addProvider(new BouncyCastleProvider());
	    try {
	    	System.out.println("try 블록 시작");
	    	logger.info("try 블록 시작.");
	        PushService pushService = new PushService(
	        		"BDoPnG6xdPyxwyzX-EQLZh8DRKfkT5jfC95knX47jyLecsZ0JLTbN0iKBrzesfPq6F_nWl7XnHb_hK-ohOEM01M",
	        		"qLYBBH1Qd7ax6uZUVFlpeU7L1LSSa_kHRpGYzIimqJU");
	        logger.info("push 블록 생성.");
	        // 알림 내용 구성
	        String payload = "{"
	            + "\"title\": \"" + userId + "님에게 알림 메세지\","
	            + "\"body\": \"" + trade.getTitle() + "\\n게시물이 등록되었습니다.\","
	            + "\"url\": \"http://localhost:8080/TestingFile/tradeInfoPage/" + trade.getTradeId() + ".do\""
	            + "}";
	        System.out.println("pay load: " + payload);
	        // 웹 푸시 알림 생성 및 전송
	        byte[] payloadBytes = payload.getBytes("UTF-8");
	        String publickey = subscription.getP256dh();
	        String authkey = subscription.getAuth();
	        String endpoint = subscription.getEndpoint();

	        // 이 부분은 PushMessage의 정의에 따라 다르게 작성될 수 있습니다.
//	        Notification pushMessage = Notification.builder()
//	                .endpoint(endpoint)
//	                .userPublicKey(publickey)
//	                .userAuth(authkey)
//	                .payload(payload)
//	                .build();
	        
	        Notification pushMessage = new Notification(
	        		subscription.getEndpoint(),
	        		subscription.getUserPublicKey(),
	        		subscription.getAuthAsBytes(),
	        		payloadBytes
	        	    );
	        logger.info("noti 블록 생성.");
	        HttpResponse httpResponse = pushService.send(pushMessage);
	        int statusCode = httpResponse.getStatusLine().getStatusCode();
	        System.out.println("상태 메시지: " + statusCode);
	        
	        if (statusCode != 201) {
	            System.out.println("status 오류");
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	        System.out.println("예외 메시지: " + e.getMessage());
	    }
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type){
        int newWidth = (int) (originalImage.getWidth() * SHRINK_FACTOR);
        int newHeight = (int) (originalImage.getHeight() * SHRINK_FACTOR);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }
	
	@RequestMapping(value="/tradeInfoPage/{tradeId}.do")//게시글 자세히보기 이동
	public ModelAndView tradeInfoPage(@PathVariable("tradeId") int tradeId) {
		ModelAndView mav = new ModelAndView("trade/tradeInfo.jsp");
		TradeVO trade = tradeService.selectTrade(tradeId);
		List<CommentVO> comments = commentService.selectCommentList(tradeId);
		
		List<CommentVO> commentslist = selectComment(comments);
		
		String commentHtml = recursiveComments(commentslist, 0);
		
		mav.addObject("trade", trade);
		
		mav.addObject("commentHtml", commentHtml);
		
		return mav;
	}
	
	@RequestMapping(value="/showComment.do", method= RequestMethod.GET)
	@ResponseBody
	public ModelAndView showComment(@RequestParam("tradeId") int tradeId) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		List<CommentVO> comments = commentService.selectCommentList(tradeId);
		
		List<CommentVO> commentslist = selectComment(comments);
		
		mav.addObject("comments", commentslist);
		System.out.println(comments.toString());
		return mav;
	}
	
	@RequestMapping("/updateTradePage.do")
	public ModelAndView tradeUpdatePage(int tradeId){
		ModelAndView mav = new ModelAndView("trade/tradeUpdate.jsp");
		
		TradeVO trade = tradeService.selectTrade(tradeId);
		mav.addObject("trade", trade);
		
		return mav;
	}
	
	@RequestMapping("/tradeUpdate.do")
	public String tradeUpdate(@ModelAttribute TradeVO trade) throws IOException {
		TradeVO tradeOrigin = tradeService.selectTrade(trade.getTradeId());
		MultipartFile uploadImage = trade.getUploadImage();
		String image=null;
		
		if (!uploadImage.isEmpty()) {
			// 파일 업로드 처리
			String originalFileName = uploadImage.getOriginalFilename();
			UUID uuid = UUID.randomUUID();	
			image=uuid+"_"+originalFileName;	
			
			uploadImage.transferTo(new File("E:\\uploads\\" + image));
			trade.setImage(image);
		}
		
		else {
			String orgFile = tradeOrigin.getImage();

			trade.setImage(orgFile);
		}
		
		tradeService.updateTrade(trade);
		
		return "redirect:/tradeInfoPage/"+Integer.toString(trade.getTradeId())+".do";
	}
	
	@RequestMapping("/tradeDelete.do")
	public String tradeDelete(int tradeId, String image) throws IOException {
		
		tradeService.deleteTrade(tradeId);
		
		File fileDel = new File("E:\\uploads\\" + image);
		fileDel.delete();
		
		return "redirect:/tradeListPage.do";
	}
	
	@RequestMapping(value="/writeComment.do", method= RequestMethod.POST)
	@ResponseBody
	public ModelAndView writeComment(CommentVO comment) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		commentService.insertComment(comment);
		
		List<CommentVO> comments = commentService.selectCommentList(comment.getTradeId());
		
		List<CommentVO> commentslist = selectComment(comments);
		
		mav.addObject("comments", commentslist);
		
		return mav;
	}
	
	@RequestMapping(value="/replywrite.do", method= RequestMethod.POST)
	@ResponseBody
	public ModelAndView replywrite(CommentVO comment) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		commentService.insertComment(comment);
		
		List<CommentVO> comments = commentService.selectCommentList(comment.getTradeId());
		
		List<CommentVO> commentslist = selectComment(comments);
		
		mav.addObject("recomments", commentslist);
		
		return mav;
	}
	
	public static String recursiveComments(List<CommentVO> comments, int indent) {
	    StringBuilder output = new StringBuilder();

	    for (CommentVO comment : comments) {
	        output.append("<div style=\"margin-left: ").append(20 * indent).append("px;\">");
	        output.append("<p>").append(comment.getWriterId()).append(": ").append(comment.getContent())
	            .append(" <button onclick=\"showReplyForm(").append(comment.getCommentId()).append(")\">작성</button></p>");

	        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
	            output.append(recursiveComments(comment.getChildren(), indent + 1));
	        }

	        output.append("</div>");
	    }

	    return output.toString();
	}
	
	public static List<CommentVO> selectComment(List<CommentVO> comments){
		
		Map<Integer, CommentVO> commentMap = new HashMap<>();
        for (CommentVO comment : comments) {
            commentMap.put(comment.getCommentId(), comment);
        }

        List<CommentVO> topLevelComments = new ArrayList<>();

        for (CommentVO comment : comments) {
            if (comment.getParentId() == 0) {
                topLevelComments.add(comment);
            } else {
                CommentVO parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment);
                }
            }
        }
        
        return topLevelComments;
	}
}
