function createOrder() {
    $.ajax({
        url: "/order",
        type: "POST",
        dataType: "json",

        accepts: {
            json: "application/json"
        },

        // on success, callback
        success: function(json) {
            var successMessage = "An order has just been created. Choose some items to add to it.";
            showResponseMessage("createOrder-success", successMessage);
            orderModel.orderId = json.id;
        },

        // on fail, callback
        error: function(xhr, status, errorThrown) {
            var failureMessage = "Failed to create a new order. Please refresh the page to try again.";
            showResponseMessage("createOrder-failure", failureMessage);

            if (console) {
                console.log("Error: " + errorThrown);
                console.log("Status: " + status);
                console.dir(xhr);
            }
        }
    });

    function showResponseMessage(elementId, message) {
        var element = $("#" + elementId);
        element.removeAttr("hidden");
        element.html(message);
    }
}
