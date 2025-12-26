package egovframework.example.cmmn;

import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class CustomContextLoaderListener extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Security.addProvider(new BouncyCastleProvider());
        super.contextInitialized(event);  // 기존 초기화 로직 호출
    }
}