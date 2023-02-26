let page = 0;
let lastCursorBoardId = $(".cursorBoardId:last").attr("id");
let scrollCheck = true;

$(window).scroll(function() {
    if ($(window).scrollTop() == $(document).height() - $(window).height()) {
      console.log(++page);

      if (scrollCheck == true) {
            storyLoad();
      }
//      $("#card").append("<h1>Page " + page + "</h1>So<BR/>MANY<BR/>BRS<BR/>YEAHHH~<BR/>So<BR/>MANY<BR/>BRS<BR/>YEAHHH~");

    }
});

var writerSearchKeyword = $('input[name=inputName]').val();

function storyLoad() {
	$.ajax({
	    type: "GET",
		url: '/boards/api/muckstarBoard?lastCursorBoardId='+ lastCursorBoardId + '&writerSearchKeyword='+writerSearchKeyword,
		dataType: "json",
		data: lastCursorBoardId,
		beforeSend: function(){
		    $('#loading').show();
		},
		success: function(result) {
		    if(result.data.last == false) {
		        scrollCheck = false;
		        return;
		    }
		    else {
		        result.data.content.forEach((boards)=>{
		            let storyItem = getStoryItem(boards);
		            $("#card").append(storyItem);
		        });
		    }
	    }
	});
}


//	}).done(res => {
//		//console.log(res);
//		res.data.content.forEach((boards)=>{
//			let storyItem = getStoryItem(boards);
//			$("#card").append(storyItem);
//		});
//	}).fail(error => {
//		console.log("오류", error);
//	});


function getStoryItem(boards) {
	let item = `<div class="story-list__item">
//	<div class="sl__item__header">
//		<div>
//			<img class="profile-image" src="/upload/${image.user.profileImageUrl}"
//				onerror="this.src='/images/person.jpeg'" />
//		</div>
//		<div>${image.writer}</div>
//	</div>

	<div class="sl__item__img">
		<img src="/upload/${image.boardFiles.storedFileName}" />
	</div>

	<div class="sl__item__contents">
		<div class="sl__item__contents__icon">

			<button>`;

			     if(image.likeState){
					item += `<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
				}else{
					item += `<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
				}


		item += `
			</button>
		</div>

		<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>

		<div class="sl__item__contents__content">
			<p>${image.caption}</p>
		</div>

		<div id="storyCommentList-${image.id}">`;

			image.comments.forEach((comment)=>{
				item +=`<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
				<p>
					<b>${comment.user.username} :</b> ${comment.content}
				</p>`;

				if(principalId == comment.user.id){
					item += `	<button onclick="deleteComment(${comment.id})">
										<i class="fas fa-times"></i>
									</button>`;
				}

			item += `
			</div>`;

			});


		item += `
		</div>

		<div class="sl__item__input">
			<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
			<button type="button" onClick="addComment(${image.id})">게시</button>
		</div>

	</div>
</div>`;
	return item;
}


function lastPostFunc()
{
    $(’div#lastPostsLoader’).html(’<img src="bigLoader.gif"/>’);
    $.post("scroll.asp?action=getLastPosts&lastID=" + $(".wrdLatest:last").attr("id"),

    function(data){
        if (data != "") {
        $(".wrdLatest:last").after(data);
        }
        $(’div#lastPostsLoader’).empty();
    });
};