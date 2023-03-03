let page = 0;
let first = true;
let scrollCheck = true;
let memberId = $(".memberId").attr("id");
let lastCursorBoardId = $(".cursorBoardId").attr("id");

const sectionHeight = document.getElementById('section-height');
let plusHeight = 0;

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
		beforeSend: function() {
		    $('#loading').show();
		},
		async: false,
		success: function(result) {
		    console.log(JSON.stringify(result))

		    if(result.last ==  true) {
                console.log("마지막 페이지");
            	scrollCheck = false;

                plusHeight += 100
		        sectionHeight.style.height = plusHeight+'vh';

            	$.each(result.content, function(index, board){
                    console.log("JSON의 내용에서 가져온 요소: ", index);

                    let muckstarItem = getStoryItem(board);
                    $("#mucstarList").append(muckstarItem);

                    lastCursorBoardId = board.id;
                 });
            }
		    else {
		        plusHeight += 100
            	sectionHeight.style.height = plusHeight+'vh';

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
    let item = `<a href="/boards/muckstarBoard/${board.id}">
                    <img class="muckstar-image" src="/imageFileUpload/${board.boardFiles[0].storedFileName}">
                </a>`;

    console.log("첫번째 이미지: ", board.boardFiles[0].storedFileName)
    console.log("가져온 요소의 출력 결과", item);

	return item;
}
