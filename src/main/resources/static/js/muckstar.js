let page = 0;
let first = true;
let scrollCheck = true;
let lastCursorBoardId = $(".cursorBoardId").attr("id");

//if($(window).scrollTop() + $(window).height() == $(document).height()) {
//    storyLoad();
//}

storyLoad();

$(window).scroll(function() {
    if ($(window).scrollTop() == $(document).height() - $(window).height()) {
      console.log(++page);

      if (scrollCheck == true) {
            storyLoad();
      }
//    넣는 방식 예제 : $("#card").append("<h1>Page " + page + "</h1>So<BR/>MANY<BR/>BRS<BR/>YEAHHH~<BR/>So<BR/>MANY<BR/>BRS<BR/>YEAHHH~");
    }
});

var writerSearchKeyword = $('input[name=inputName]').val();

function storyLoad() {
	$.ajax({
	    type: "GET",
		url: '/boards/api/muckstarBoard',
		dataType: "json",
		data: {lastCursorBoardId, first},
		beforeSend: function() {
		    $('#loading').show();
		},
		async: false,
		success: function(result) {
		    console.log(JSON.stringify(result))

		    if(result.last ==  true) {
		        console.log("마지막 페이지");
		        scrollCheck = false;

		        $.each(result.content, function(index, board){
                    console.log("JSON의 내용에서 가져온 요소: ", index);

                	let muckstarItem = getStoryItem(board);
                	$("#mucstarList").append(muckstarItem);

                	lastCursorBoardId = board.id;
                });

		    }
		    else {
		        $.each(result.content, function(index, board){
		            console.log("JSON의 내용에서 가져온 요소: ", index);

		            let muckstarItem = getStoryItem(board);
		            $("#mucstarList").append(muckstarItem);

		            lastCursorBoardId = board.id;
		        });
		        if(first) {
                    first = false;
                }
		    }
	    },
	    error: function (error) {
            console.log("오류", error);
        }
	});
}


function getStoryItem(board) {
    let date = new Date(board.createdDate);
    console.log(board.createdDate)

    let nowDate = new Date();
    console.log(nowDate);

    var comparedDate = dateCompare(date, nowDate);

    let item = `<div class="mucstarList_item">
                    <table>
                        <tr>
                            <td> <img src="/image/muckstargram_img/muckstar_background.PNG" style="width: 23vw;"></td>

                            <td>
                                <div class="card" id="card" style="margin-top: 10%; left: 10%; width: 40vw; height: 70vh;">
                                    <a href="/boards/muckstarBoard/${board.id}">
                                        <img class="muckstar-image" src="/imageFileUpload/${board.boardFiles[0].storedFileName}"
                                             style="width: 40vw; height: 62vh;">
                                    </a>

                                    <div class="input-group" style="margin-left: 5%;">
                                        <button class="btn btnEvent" style="width: 3vw; height: 3vh;"
                                                id="${board.id}"
                                                onclick="likeUpdate(this);" type="button">
                                            <img src="/image/muckstargram_img/favorite_icon.PNG" style="width: 3vw; height: 5vh;">
                                        </button>

                                        <span class="like" id="likeCount${board.id}" style="font-weight: 500; font-size: 250%; margin-left: 5%;">${board.likeCount}</span>

                                        <img src="/image/muckstargram_img/comment_icon.PNG" style = "width: 3vw; height: 5vh; margin-left: 5%; margin-top:1%">
                                        <span class="comment" style ="font-weight: 500; font-size: 250%; margin-left: 3%;">${board.commentCount}</span>

                                        <span id="date" style="float: right; margin-left: 40%;">${comparedDate}</span>
                                    </div>
                                </div>
                            </td>

                            <td>
                                <img src="/image/food/sin.jpg" style="margin-left: 30%; width: 20vw;">
                            </td>
                        </tr>
                    </table>
                </div>`;

    console.log("첫번째 이미지: ", board.boardFiles[0].storedFileName)
    console.log("가져온 요소의 출력 결과", item);

	return item;
}


function likeUpdate(boardId) {
    var boardId = boardId.id;
    var likeCount = document.getElementById('likeCount' + boardId);

    console.log("board=", boardId)
    console.log("userId=", userId)

    if (userId === 'GuestId'){
        var confirmMessage = `회원만 좋아요를 누를 수 있습니다. 로그인 먼저 해주세요!`;
        if (confirmMessage(confirmMessage)) {
            var loginUrl = `/members/login`;
            location.href = loginUrl;
        }
    }
	else {
        $.ajax({
            type: "GET",
            url: '/boards/api/muckstarBoard/' + boardId + '/like',
            dataType: "json",
            data: {userId: userId},
            async: false,
            success: function(result) {
                console.log(JSON.stringify(result))
                likeCount.innerText = JSON.stringify(result);
            },
            error: function (error) {
                console.log("오류", error);
            }
        });
	}
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

//     // 다중 파일 모두 출력하기
//     // 1. $.each 반복 방식
//     $.each(board.boardFiles, function(index, boardFile){
//               item += `<img class="muckstar-image" src="/imageFileUpload/${boardFile.storedFileName}"
//                             style="max-width: 100%; height: 90%;">`;
//     console.log(index+"번째 이미지 파일 = ", boardFile.storedFileName);
//     })

//     // 2, 배열변수.forEach 반복 방식
//     board.boardFiles.forEach((boardFile)=>{
//               item += `<img class="muckstar-image" src="/imageFileUpload/${boardFile.storedFileName}"
//                             style="max-width: 100%; height: 90%;">`;
//     });

