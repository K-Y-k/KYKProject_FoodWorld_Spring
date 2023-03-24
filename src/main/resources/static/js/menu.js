var circle = document.getElementById("circle");
var up_btn = document.getElementById("up_btn");
var down_btn = document.getElementById("down_btn");

var rotateValue = circle.style.transform;
var rotateSum;

up_btn.onclick = function() {      // 카테고리 회줜을 원으로 배치하여 사용한 기능 클릭시 90도 회전하게 함
	rotateSum = rotateValue + "rotate(-90deg)";
	circle.style.transform = rotateSum;
	rotateValue = rotateSum;
}

down_btn.onclick = function() {
	rotateSum = rotateValue + "rotate(90deg)";
	circle.style.transform = rotateSum;
	rotateValue = rotateSum;
}


const menu = [  // 메뉴, 식당, 이미지를 배열 항목에 선정
		{ name : "한식, 백반", franchise : "홍장금백반전문점", menu_img  : "/image/menu_img/01.PNG" },
		{ name : "분식", franchise : "김밥천국, 고봉민김밥, 청년분식", menu_img  : "/image/menu_img/02.PNG"  },
		{ name : "중식", franchise : "왕비성, 홍콩반점, 사대천왕", menu_img  : "/image/menu_img/03.PNG" },
		{ name : "치킨", franchise : "bhc, bbq, 교촌, 지코바", menu_img  : "/image/menu_img/04.PNG" },
		{ name : "햄버거", franchise : "맘스터치, 버거킹, 맥도날드, 롯데리아", menu_img  : "/image/menu_img/05.PNG" },
		{ name : "샌드위치", franchise : "서브웨이, 시나피샌드위치", menu_img  : "/image/menu_img/06.PNG" },
		{ name : "떡볶이", franchise : "엽기떡볶이, 신떡, 달토끼", menu_img  : "/image/menu_img/07.PNG" },
		{ name : "초밥", franchise : "상무초밥, 청춘초밥, 스시하린, 다해초밥", menu_img  : "/image/menu_img/08.PNG" },
		{ name : "피자", franchise : "피자헛, 도미노피자, 미스터피자, 파파존스 ", menu_img  : "/image/menu_img/09.PNG" },
		{ name : "족발, 보쌈", franchise : "도미보쌈족발, 대박족발, 명품족발&보쌈", menu_img  : "/image/menu_img/10.PNG" },
		{ name : "곱창", franchise : "낙곱새, 오복곱창, 곱분이곱창", menu_img  : "/image/menu_img/11.PNG" },
		{ name : "돈까스", franchise : "홍카츠, 너무커서놀란돈까스&별리달리", menu_img  : "/image/menu_img/12.PNG" },
		{ name : "국밥, 감자탕", franchise : "이순신소국밥, 아구쪽 ", menu_img  : "/image/menu_img/13.PNG" },
		{ name : "닭갈비", franchise : "일미닭갈비, 5.5닭갈비, 장인닭갈비 ", menu_img  : "/image/menu_img/14.PNG" }
]


function randomMenu() {  // 랜덤 메뉴 함수 선언
	const randomMenu = Math.floor(Math.random() * menu.length); // 선언한 개수 안으로 랜덤 범위를 지정하기 위해 + 소수점 제거를 위해 버림 함수 적용

	const menu_name = document.getElementById("menu_name"); // 화면에 나타나게 할  메뉴, 식당 id를 가져온 변수 선언
	const franchies = document.getElementById("franchies");
	const menu_img = document.getElementById("menu_img");

	menu_name.innerText = menu[randomMenu].name;            // 해당 변수 안의 내용을 랜덤하게 출력 적용
	franchise.innerText = menu[randomMenu].franchise;
	menu_img.src = menu[randomMenu].menu_img;
}

const random_btn = document.getElementById("random_btn"); // 버튼을 동작하기 위한 id를 가져온 변수 선언
random_btn.addEventListener("click", randomMenu);         // 클릭 시 랜덤 메뉴 함수 작동