var temp1
var temp2
var count = 0

function updateComment(commentId) {
    if (count == 0){  // 자기가 작성한 댓글이 여러개 일 때 쳐음 댓글에 수정작업에 들어갈 때
        var commentContentElement = document.getElementById('commentContent_' + commentId);
        var commentInputElement = document.getElementById('commentContentInput_' + commentId);
        temp1 = document.getElementById('commentContent_' + commentId);
        temp2 = document.getElementById('commentContentInput_' + commentId);

        commentContentElement.style.display = 'none';
        commentInputElement.style.display = 'block';
        count += 1
    }
    else { // 자기가 작성한 댓글이 여러개 일 때 댓글에 수정을 걸어놓은 상태일 때 다른 댓글에 수정작업을 들어가면 기존 댓글의 수정작업을 닫기 위한 필터링
        temp1.style.display = 'block';
        temp2.style.display = 'none';

        var commentContentElement = document.getElementById('commentContent_' + commentId);
        var commentInputElement = document.getElementById('commentContentInput_' + commentId);

        commentContentElement.style.display = 'none';
        commentInputElement.style.display = 'block';
        count = 0
    }
}