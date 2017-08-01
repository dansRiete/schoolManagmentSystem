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
            $("#avgButton").on("click", app.showModal);
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
        }

    };
    app.initialize();
}) ();
