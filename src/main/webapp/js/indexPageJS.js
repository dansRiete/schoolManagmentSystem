/**
 * Created by Aleks on 01.08.2017.
 */
(function () {
    let app = {
        initialize(){
            console.log("initialize()");
            app.setListeners();
        },
        setListeners(){
            $("#resetButton").on("click", app.resetHandler);
            $("#avgButton").on("click", app.submit);
        },
        resetHandler(){
            console.log("click");
            let form = $("#filterForm");
            console.log(form);
            form[0].reset();
            $("#subjectsSelect").val(null);
            $("#selectedDate").val(null);
            form[0].submit();

        },
        showModal(){
            console.log("inside showModal()");
            $('#myModal').modal({
                show: true
            });
        }, submit(e){
            e.preventDefault();
            let form = $("#filterForm");
            let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/average";
            console.log("Link=" + link);
            $.ajax({
                url: link,
                type: "POST",
                data: form.serialize()
            }).fail(function () {
                console.log("Fail");
            }).always(function (msg) {
                console.log(msg["status"]);
            }).done(function (msg) {
                console.log(msg["status"]);
            });
        }

    };
    app.initialize();
}) ();
