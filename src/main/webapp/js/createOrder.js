function createOrder() {
    $.ajax({
        url: "/order",
        type: "POST",
        dataType: "json",

        // on success, callback
        success: function(json) {
            $("#order-create-text")
                .html("Order number " + json.id + " has just been created. Choose items to add to the order.");
        },

        // on fail, callback
        error: function(xhr, status, errorThrown) {
            alert("An error occurred, so an order hasn't been created. \nPlease refresh the page to try again.");
            console.log("Error: " + errorThrown);
            console.log("Status: " + status);
            console.dir(xhr);
        },
    });
}
