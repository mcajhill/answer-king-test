// simple model object for keeping track of the state of the current order
function OrderModel() {
    this.orderId = null;

    this.paid = false;

    this.itemsCollection = [];

    this.receipt = null;
}