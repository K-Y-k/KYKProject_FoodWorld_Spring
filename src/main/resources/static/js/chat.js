var connectingElement = document.querySelector('.connecting');
var receiver = $("#receiver").val();
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

console.log("방 값: ", roomId)

if (roomId != null) {
    console.log("방 찾았으니 실행하자")
    connect()
}


function connect(event) {
    console.log("현재 회원 이름: ", username)
    console.log("상대 회원: ", receiver)
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

// 메시지 전송시
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

// 에러 발생시
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
    console.log("payload 정보 : " + payload);
    var chat = JSON.parse(payload.body);

    if (chat.type === 'ENTER') {       // enter라면
        chat.content = chat.sender + chat.message;

        let enterMessage = `<li style="list-style-type: none; text-align: center; color: #777;">`
                             + chat.content +
                          `</li>`

        $('#chatContent').append(enterMessage);
    } else if (chat.type === 'LEAVE') { // leave라면
        chat.content = chat.sender + chat.message;

        let leaveMessage = `<li style="list-style-type: none; text-align: center; color: #777;">`
                             + chat.content +
                          `</li>`

        $('#chatContent').append(leaveMessage);
    } else {                           // talk라면
        let date = new Date();
        let nowTime = createTime(date);

        if (chat.senderId == userId){ // 본인이 보낸 것이면
            let talkMessage = `<li style="list-style-type: none; border-radius: 5px;">
                                 <table style="float: right;">
                                    <tr style="background: red;">
                                        <td>
                                            <span style="font-size: 15px; margin-right: 30px; padding: 0px 5px 0px 5px">`
                                                + nowTime +
                                            `</span>
                                         </td>

                                         <td>
                                            <span style="background-color: lightyellow; padding: 0px 5px 0px 5px; font-size : 25px;">`
                                                + chat.message +
                                            `</span>
                                        </td>
                                    </tr>
                                 </table>
                               </li>`

            $("#chatContent").append(talkMessage);
        } else { // 상대가 보낸 것이면
            let talkMessage = `<li style="list-style-type: none; border-radius: 5px;">
                                    <table style="float: left;">
                                        <tr>
                                            <td>
                                                <img src="/image/muckstargram_img/user_icon.PNG" style="width: 5vw; height: 8vh;"></img>
                                            </td>

                                            <td>
                                                <table>
                                                    <tr style="background: red;">`
                                                        + chat.sender +
                                                    `</tr>

                                                    <tr>
                                                        <td>
                                                            <span style="background-color: violet; padding: 0px 5px 0px 5px; font-size : 25px;">`
                                                                + chat.message +
                                                            `</span>
                                                        </td>

                                                        <td>
                                                             <span style="font-size: 15px; margin-right: 30px; padding: 0px 5px 0px 5px">`
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


function handleKeyDown(event) {
    if (event.keyCode === 13) {
      sendMessage()
    }
  }

