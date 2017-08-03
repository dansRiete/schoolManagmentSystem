/**
 * Created by Aleks on 01.08.2017.
 */
(function () {
    let app = {
        initialize(){
            console.log('initialize');
            app.setListeners();
        },
        setListeners(){
            $("#resetButton").on("click", app.resetHandler);
            $("#avgButton").on("click", app.submit);
        },
        resetHandler(){
            let form = $("#filterForm");
            console.log(form);
            form[0].reset();
            $("#subjectsSelect").val(null);
            $("#selectedDate").val(null);
            form[0].submit();

        },
        showAverageGradeModal(){
            $('#myModal').modal({show: true});
        }, submit(e){
            e.preventDefault();
            let form = $("#filterForm");
            let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/average";
            $.ajax({url: link, type: "POST", data: form.serialize()})
                .fail(function () {console.log("Sending form failed")})
                .done(function (msg) {
                    if(msg["avg"] !== null && msg["avg"] !== undefined){
                        console.log('first');
                        console.log(msg["avg"]);
                        $("#modal-body-text").html(msg["avg"]);
                        $("#modal-title-text").html(
                            'Average mark on:' +
                            (msg["subjectTitle"] === '' ? '\r\nSubject: all subjects' : ("\r\nSubject - " + msg["subjectTitle"] + ';')) +
                            (msg["selectedDate"] === '' ? '\r\nDate: all dates' :   ("\r\nDate - " + msg["selectedDate"] + ';'))
                        );
                    }else {
                        console.log('second');
                        $("#modal-body-text").html('There are no grades on requested conditions');
                        $("#modal-title-text").html(
                            'Average mark on:' +
                            (msg["subjectTitle"] === '' ? '\r\nSubject: all subjects' : ("\r\nSubject - " + msg["subjectTitle"] + ';')) +
                            (msg["selectedDate"] === '' ? '\r\nDate: all dates' :   ("\r\nDate - " + msg["selectedDate"] + ';'))
                        );
                    }

                    app.showAverageGradeModal();
                });
        }

    };
    app.initialize();
}) ();
