// dynamically loads the table based on the incoming JSON from url /item
function loadTable() {
    $('#items-table').bootstrapTable({
        url: "/item",
        method: "get",

        columns: [{
            field: "productImage",
            class: "image"
        },  {
            field: "name",
            title: "Item"
        }, {
            field: "price",
            title: "Price"
        }, {
            field: "quantity",
            title: "Quantity Required",
            class: "quantity"
        }, {
            field: "addToCart",
            title: "Add to Cart",
            class: "addToCart"
        }],

        onLoadSuccess: applyStaticData
    });
}

function applyStaticData() {
    addImages();
    addQuantitySelectors();
    addCartButton();
}

// adds the cell data provided by the callback to all rows in the table body
function integrateCellData(className, callback) {
    var cells = document.getElementsByClassName(className);

    // iterate through all td elements in order to place the static content
    for (var i = 1; i < cells.length; i++) {
        var row = cells.item(i);
        row.removeChild(row.firstChild);    // remove the "-" placed in the td during dynamic creation
        row.appendChild(callback());
    }
}

function addImages() {
    integrateCellData("image", getProductImage);

    // convenience method for creating an <img>
    function getProductImage() {
        var image = new Image();
        image.src = "../images/logo.jpg";
        image.alt = "Product Image";
        image.height = 75;
        image.width = 75;
        return image;
    }
}

function addQuantitySelectors() {
    integrateCellData("quantity", getQuantitySelector);

    function getQuantitySelector() {
        var label = document.createElement("LABEL");
        label.innerHTML = "Quantity: ";

        var input = document.createElement("INPUT");
        input.setAttribute("class", "quantity-input");
        input.setAttribute("type", "number");
        input.setAttribute("placeholder", "0");
        input.setAttribute("max", "15");
        input.setAttribute("min", "0");

        var div = document.createElement("DIV");
        div.appendChild(label);
        div.appendChild(input);

        return div;
    }
}

function addCartButton() {
    integrateCellData("addToCart", getAddToCartButton);

    function getAddToCartButton() {
        var div = document.createElement("DIV");
        div.setAttribute("class", "cart-button");
        div.innerHTML = "Add to Cart";
        return div;
    }
}