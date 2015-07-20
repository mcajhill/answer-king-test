function submitPayment() {
    var items = orderModel.itemsCollection;

    if (items.length > 0) {
        var paymentAmount = $("#payment-amount")[0].value;
        var url = "/order/" + orderModel.orderId + "/pay";

        $.ajax({
            url: url,
            method: "PUT",
            data: paymentAmount,
            contentType: "application/json; charset=UTF-8",

            success: function(json) {
                // if the failure message is still there, hide it
                $("#pay-failure").attr("hidden", "hidden");

                // show the success message
                var successDiv = $("#pay-success").removeAttr("hidden");
                successDiv.html("Payment success. Your change is " + "&pound;" + json.change + ". Enjoy your meal!");

                // update the model
                orderModel.paid = true;
                orderModel.receipt = json;
            },

            error: function() {
                $("#pay-success").attr("hidden", "hidden");

                var failureDiv = $("#pay-failure").removeAttr("hidden");
                failureDiv.html("There was an error with this payment. Please refresh the page and try again.");
            }
        });
    }
}