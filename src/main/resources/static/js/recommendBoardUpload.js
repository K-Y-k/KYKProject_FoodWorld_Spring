var select = document.getElementById("category-choice");
var restaurantArea = document.getElementById("restaurant-area");
var menuChoice = document.getElementById("menu-choice");

function categoryHandleChange() {
    var selectedIndex = select.selectedIndex;
    var selectedOption = select.options[selectedIndex];
    var selectedCategory = selectedOption.value;

    if (selectedCategory == '카테고리 선택') {
        restaurantArea.style.display = 'none';
        restaurantArea.value = '';

        menuChoice.style.display = 'none';
        menuChoice.value = '';
    }

    if (selectedCategory == '식당') {
        restaurantArea.style.display = 'block';
        menuChoice.style.display = 'none';
        menuChoice.value = '';
    }

    if (selectedCategory == '메뉴') {
        menuChoice.style.display = 'block';
        restaurantArea.style.display = 'none';
        restaurantArea.value = '';
    }

}