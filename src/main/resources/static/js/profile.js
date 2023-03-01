let page = 0;
let first = true;
let scrollCheck = true;
let memberId = $(".memberId").attr("id");
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


function storyLoad() {
	$.ajax({
	    type: "GET",
		url: '/members/api/muckstarBoard',
		dataType: "json",
		data: {lastCursorBoardId, memberId, first},
		beforeSend: function(){
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
    let item = `<img class="muckstar-image" src="/imageFileUpload/${board.boardFiles[0].storedFileName}">`;

    console.log("첫번째 이미지: ", board.boardFiles[0].storedFileName)
    console.log("가져온 요소의 출력 결과", item);

	return item;
}

function resize(obj) {
    obj.style.height = '1px';
    obj.style.height = (12 + obj.scrollHeight) + 'px';
}