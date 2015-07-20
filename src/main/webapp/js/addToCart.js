function addItemToCart(cartButton) {
    var row = cartButton.closest("tr");

    // find the corresponding item data from the row
    var itemId = $(row).children(".item-id")[0].innerHTML;
    var itemPrice = $(row).children(".item-price")[0].innerHTML;
    var itemQty = row.querySelector("input").value;

    // only add to the order model if there is a quantity
    if (itemQty && itemQty > 0) {
        addItemToOrderModel();
        updateServer();
        sendSuccessMessage();
        updateCart();
    }

    function updateServer() {
        var url = "/order/" + orderModel.orderId + "/addItem/" + itemId;

        $.ajax({
            url: url,
            type: "PUT",
            data: itemQty,
            contentType: "application/json; charset=UTF-8",

            error: function(xhr, status, errorThrown) {
                if (console) {
                    console.log("Error: " + errorThrown);
                    console.log("Status: " + status);
                    console.dir(xhr);
                }
            }
        });
    }

    function addItemToOrderModel() {
        var items = orderModel.itemsCollection;
        var newItem = new Item(itemId, itemPrice, itemQty);
        var itemIndex = -1;

        // if itemsCollection is empty, push without question
        if (items.length === 0) {
            items.push(newItem);
        }
        else {
            // find the index of the item, if it's there
            $.each(items, function (index, item) {
                if (item.id === itemId) {
                    itemIndex = index;
                }
            });

            // update quantity, or add new element
            if (itemIndex >= 0) {
                items[itemIndex].quantity = itemQty;
            }
            else {
                items.push(newItem);
            }
        }
    }

    function sendSuccessMessage() {
        var addToCartMessage = $(row).children(".add-to-cart").children(".added-to-cart-message")[0];
        addToCartMessage.removeAttribute("hidden");
    }

    function updateCart() {
        var totalCost = 0;

        $.each(orderModel.itemsCollection, function(index, item) {
            totalCost += (item.quantity * item.price);
        });

        $("#order-total").html("&pound;" + totalCost.toPrecision(3));
    }
}