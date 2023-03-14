var connectingElement = document.querySelector('.connecting');
var receiver = $("#receiver").val();
var chatContent = document.querySelector('#chatContent');
var messageInput = document.querySelector('#message');

var stompClient = null;


// roomId 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

console.log("방 값: ", roomId)

if (roomId != null) {
    connect()
}


// 소켓 실행 시
function connect() {
    console.log("현재 회원 이름: ", username)
    console.log("상대 회원: ", receiver)
    console.log("방 이름: ", roomId)


    if(username) {
        var socket = new SockJS('/ws-stomp');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }

}

// 소켓 연결 시
function onConnected() {
    // sub 할 url => /sub/chat/room?roomId로 구독하여 해당 경로로 메시지 발송시 onMessageReceived 함수 실행
    stompClient.subscribe('/sub/chat/room/'+ roomId, onMessageReceived);

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

    connectingElement.style.display='none';
}

// 회원 퇴장시
function onLeave() {
    stompClient.subscribe('/sub/chat/room/'+ roomId, onMessageReceived);

    stompClient.send("/pub/chat/leaveUser",
            {},
            JSON.stringify({
                "roomId": roomId,
                sender: username,
                senderId: userId,
                type: 'LEAVE'
            })
        )

    connectingElement.style.display='none';
}

// 에러 발생시
function onError(error) {
    connectingElement.style.display = 'block';
    connectingElement.textContent = '연결이 원할하지 않습니다. 다시 참여해주세요!';
    connectingElement.style.color = 'red';
}

// 메시지 전송시 : 메시지 전송할 때는 JSON 형식을 메시지를 서버에 전달한다.
function sendMessage() {
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

}

// 서버에서 보내온 메시지 받기
// 메시지를 받을 때도 마찬가지로 JSON 타입으로 받으며, 넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    console.log("payload 정보 : " + payload);
    var chat = JSON.parse(payload.body);

    if (chat.type === 'ENTER') {       // enter라면
        chat.content = chat.message;

        let enterMessage = `<li style="list-style-type: none; text-align: center; color: white;">`
                             + chat.content +
                           `</li>`

        $('#chatContent').append(enterMessage);
    } else if (chat.type === 'LEAVE') { // leave라면
        chat.content = chat.message;

        let leaveMessage = `<li style="list-style-type: none; text-align: center; color: white;">`
                             + chat.content +
                           `</li>`

        $('#chatContent').append(leaveMessage);
    } else {                           // talk라면
        let date = new Date();
        let nowTime = createTime(date);

        if (chat.senderId == userId){ // 본인이 보낸 것이면
            let talkMessage = `<li style="list-style-type: none; border-radius: 5px; margin-top: 3%;">
                                 <table style="float: right;">
                                    <tr>
                                        <td style="vertical-align: bottom;">
                                            <span style="font-size: 15px; padding: 0px 5px 0px 5px;">`
                                                + nowTime +
                                            `</span>
                                         </td>

                                         <td>
                                            <span style="background-color: lightyellow; padding: 5px 10px 5px 10px; font-size: 25px; border-radius: 1.0rem;">`
                                                + chat.message +
                                            `</span>
                                        </td>
                                    </tr>
                                 </table>
                               </li>`

            $("#chatContent").append(talkMessage);
        } else { // 상대가 보낸 것이면
            let talkMessage = `<li style="list-style-type: none; border-radius: 5px; margin-top: 3%;">
                                    <table style="float: left;">
                                        <tr>
                                            <td>
                                                <img src="/image/muckstargram_img/user_icon.PNG" style="width: 5vw; height: 8vh;"/>
                                            </td>

                                            <td>
                                                <table>
                                                    <tr>`
                                                        + chat.sender +
                                                    `</tr>

                                                    <tr>
                                                        <td>
                                                            <span style="background-color: violet; padding: 5px 10px 5px 10px; font-size: 25px; border-radius: 1.0rem;">`
                                                                + chat.message +
                                                            `</span>
                                                        </td>

                                                        <td style="vertical-align: bottom;">
                                                             <span style="font-size: 15px; margin-right: 30px; padding: 0px 5px 0px 5px;">`
                                                                + nowTime +
                                                             `</span>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                 </li>`

            $("#chatContent").append(talkMessage);
        }
    }

    chatContent.scrollTop = chatContent.scrollHeight;
}


// 메시지 전송을 키보드 엔터키로 누를 때
function handleKeyDown(event) {
    if (event.keyCode === 13) {
      sendMessage()
    }
 }


// 날짜 생성
function createTime(date){
    let hour = date.getHours();
    let minute = date.getMinutes();

    if (hour < 10) {
        hour = '0' + hour;
    }

    if (minute < 10) {
        minute = '0' + minute;
    }

    let formattedTime = hour + ':' + minute;

    return formattedTime;
}
// 날짜 시:분 형태로 변형
function dateCompare(date, nowDate) {
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hour = date.getHours();
        let minute = date.getMinutes();

        month = month >= 10 ? month : '0' + month;
        day = day >= 10 ? day : '0' + day;
        hour = hour >= 10 ? hour : '0' + hour;
        minute = minute >= 10 ? minute : '0' + minute;

        let nowYear = nowDate.getFullYear();
        let nowMonth = nowDate.getMonth() + 1;
        let nowDay = nowDate.getDate();

        nowMonth = month >= 10 ? month : '0' + month;
        nowDay = day >= 10 ? day : '0' + day;

        if (year == nowYear && month == nowMonth && day == nowDay) {
            return hour + ':' + minute;
        } else {
            return year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
        }
}

