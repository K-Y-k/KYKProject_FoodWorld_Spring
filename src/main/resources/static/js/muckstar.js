let page = 0;
let lastCursorBoardId = $(".cursorBoardId").attr("id");
let scrollCheck = true;

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
		data: {lastCursorBoardId},
		beforeSend: function(){
		    $('#loading').show();
		},
		async: false,
		success: function(result) {
		    console.log(JSON.stringify(result))
		    if(result.last ==  true) {
		        console.log("마지막 페이지");
		        scrollCheck = false;
		        return;
		    }
		    else {
		        $.each(result.content, function(index, board){
		            console.log("JSON의 내용에서 가져온 요소: ", index);
		            let muckstarItem = getStoryItem(board);
		            $("#mucstarList").append(muckstarItem);

		            if(index + 1 == result.size){
		                lastCursorBoardId = board.id;
		            }
		        });
		    }
	    },
	    error: function (error) {
            console.log("오류", error);
        }
	});
}


function getStoryItem(board) {
    let item = `<div class="mucstarList__item">
                    <div class="card" id="card" style="margin-top: 10%; left: 40%; width: 40vw; height: 70vh margin-top: 10%;">
                        <div class="cursorBoardId" th:id="${board.id}" style="display: none;" th:name="${board.id}"></div>`;

    // 다중 파일 업로드 기능이 있기에 [0]항으로 첫번째 이미지 파일을 하나 가져오기
               item += `<img class="muckstar-image" src="/imageFileUpload/${board.boardFiles[0].storedFileName}"
                             style="max-width: 100%; height: 90%;">`;
                             console.log("첫번째 이미지: ", board.boardFiles[0].storedFileName)
            item += `</div>
                 </div>`;

    console.log("가져온 요소의 출력 결과", item);

	return item;
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

