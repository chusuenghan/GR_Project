self.addEventListener('install', function(event) {
    console.log('Service Worker: Installed');
});

self.addEventListener('activate', function(event) {
    console.log('Service Worker: Activated');
});

self.addEventListener('push', function(event) {
	console.log('Received push data:', event.data.text());
    const payload = event.data.json();
    var options = {
        body: payload.body,
		data: {
            url: payload.url
        }
        // title은 Notification API에서 사용되지 않습니다.
        // 대신, showNotification의 첫 번째 인자로 전달됩니다.
    };

    event.waitUntil(
        self.registration.showNotification(payload.title, options)
    );
});

self.addEventListener('notificationclick', function(event) {
    event.notification.close();  // 알림을 닫습니다.
    var urlToOpen = event.notification.data.url;
	if (urlToOpen === null){ return;}
    event.waitUntil(
        clients.matchAll({
            type: 'window',
            includeUncontrolled: true
        }).then(function(windowClients) {
            var matchingClient = null;

            for (var i = 0; i < windowClients.length; i++) {
                var windowClient = windowClients[i];
                if (windowClient.url === urlToOpen) {
                    matchingClient = windowClient;
                    break;
                }
            }

            if (matchingClient !== null) {
                return matchingClient.focus();  // 이미 열려있는 탭에 포커스
            } else {
                return clients.openWindow(urlToOpen);  // 새 탭에서 URL 열기
            }
        })
    );
});