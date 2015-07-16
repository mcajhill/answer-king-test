// dynamically loads the table based on the incoming JSON from url /item
var ItemDataRetriever = {
    handlerData: function(json) {
        var templateSource = $("#items-table-template").html();
        var template = Handlebars.compile(templateSource);
        var studentHTML = template(json);

        $('#items-table-container').html(studentHTML);
    },

    loadItems: function() {
        $.ajax({
            url: "/item",
            method: 'get',
            success: this.handlerData
        });
    }
};
