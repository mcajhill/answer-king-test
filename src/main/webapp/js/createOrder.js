function createOrder() {
    $.ajax({
        url: "/order",
        type: "POST",
        dataType: "json",

        accepts: {
            json: "application/json"
        },

        // on success, callback
        success: function() {
            var successMessage = "An order has just been created. Choose some items to add to it.";
            showResponseMessage("createOrder-success", successMessage);
        },

        // on fail, callback
        error: function(xhr, status, errorThrown) {
            var failureMessage = "Failed to create a new order. Please refresh the page to try again.";
            showResponseMessage("createOrder-failure", failureMessage);

            console.log("Error: " + errorThrown);
            console.log("Status: " + status);
            console.dir(xhr);
        }
    });

    function showResponseMessage(elementId, message) {
        var messageElement = document.getElementById(elementId);
        messageElement.removeAttribute("hidden");
        messageElement.innerHTML = message;
    }
}
