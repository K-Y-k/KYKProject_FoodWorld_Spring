var restaurantArea = document.getElementById("restaurant-area");
var menuChoice = document.getElementById("menu-choice");
var select = document.getElementById("category-choice");

function categoryHandleChange() {
    var selectedIndex = select.selectedIndex;
    var selectedOption = select.options[selectedIndex];
    var selectedCategory = selectedOption.value;

    if (selectedCategory == '카테고리 선택') {
        console.log('뭐지', selectedCategory)
        restaurantArea.style.display = 'none';
        menuChoice.style.display = 'none';
    }

    if (selectedCategory == '식당') {
        restaurantArea.style.display = 'block';
        menuChoice.style.display = 'none';
        $.ajax({
            type: "GET",
            url: '/boards/recommendBoard',
            dataType: "html",
            data: {selectedCategory},
            beforeSend: function() {
            },
            async: false,
            success: function(result) {
                console.log("성공 ")
            },
            error: function (error) {
                console.log("오류", error);
            }
        });
    }

    if (selectedCategory == '메뉴') {
        menuChoice.style.display = 'block';
        restaurantArea.style.display = 'none';
    }

}