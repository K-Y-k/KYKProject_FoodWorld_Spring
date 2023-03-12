var connectingElement = document.querySelector('.connecting');
var chatContent = document.querySelector('#chatContent');
var messageInput = document.querySelector('#message');

var stompClient = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// roomId 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

console.log("방 값 = ", roomId)

if (roomId != null) {
    console.log("방 찾았으니 실행하자")
    connect()
}


function connect(event) {
    console.log("현재 회원 이름: ", username)
    console.log("방 이름: ", roomId)

    if(username) {
        var socket = new SockJS('/ws-stomp');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    // sub 할 url => /sub/chat/room?roomId로 구독하여 해당 경로로 메시지 발송시 onMessageReceived함수 실행
    stompClient.subscribe('/sub/chat/room/'+roomId, onMessageReceived);

    // 서버에 username을 가진 유저가 들어왔다는 것을 알림 즉, /pub/chat/enterUser로 메시지를 보냄
    stompClient.send("/pub/chat/enterUser",
            {},
            JSON.stringify({
                "roomId": roomId,
                sender: username,
                senderId: userId,
                type: 'ENTER'
            })
        )

    connectingElement.style.visibility='hidden';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            senderId: userId,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }

    event.preventDefault();
}

function onError(error) {
    connectingElement.style.visibility='visible';
    connectingElement.textContent = '연결이 원할하지 않습니다. 다시 참여해주세요!';
    connectingElement.style.color = 'red';
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            senderId: userId,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

// 메시지를 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    console.log("payload 들어오냐? : "+payload);
    var chat = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (chat.type === 'ENTER') {  // enter라면
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;

    } else if (chat.type === 'LEAVE') { // leave라면
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;

    } else { // talk라면
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(chat.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(chat.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    chatContent.appendChild(messageElement);
    chatContent.scrollTop = chatContent.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}
