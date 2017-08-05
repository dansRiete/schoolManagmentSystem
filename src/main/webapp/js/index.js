/**
 * Created by Aleks on 01.08.2017.
 */
(function () {
    let app = {
        initialize(){
            app.setListeners();
        },
        setListeners(){
            $("#resetButton").on("click", app.resetHandler);
            $("#addNewGradeButton").on("click", app.showAddGradeModal);
            $("#avgButton").on("click", app.submitAverage);
        },
        resetHandler(){
            let form = $("#filterForm");
            form[0].reset();
            $("#subjectsSelect").val(null);
            $("#selectedDate").val(null);
            form[0].submit();

        },showAddGradeModal(){
            $('#modalAddGrade').modal({show: true});
        },
        showAverageGradeModal(){
            $('#myModal').modal({show: true});
        },
        submitAverage(e){
            e.preventDefault();
            let form = $("#filterForm");
            let link = $(location).attr("protocol") + "//" + $(location).attr("host") + "/average";
            $.ajax({
                url: link, type: "POST", data: form.serialize()
            })
                .fail(function () {
                    console.log("Sending form failed")
                })
                .done(function (msg) {
                    if(msg["avg"] !== null && msg["avg"] !== undefined){
                        $("#modal-body-text").html(msg["avg"]);
                        $("#modal-title-text").html(
                            'Average mark on:' +
                            (msg["subjectTitle"] === '' ? '\r\nSubject: all subjects' : ("\r\nSubject - " + msg["subjectTitle"] + ';')) +
                            (msg["selectedDate"] === '' ? '\r\nDate: all dates' :   ("\r\nDate - " + msg["selectedDate"] + ';'))
                        );
                    }else {
                        $("#modal-body-text").html('There are no grades on requested conditions');
                        $("#modal-title-text").html(
                            'Average mark on:' +
                            (msg["subjectTitle"] === '' ? '\r\nSubject: all subjects' : ("\r\nSubject - " + msg["subjectTitle"] + ';')) +
                            (msg["selectedDate"] === '' ? '\r\nDate: all dates' :   ("\r\nDate - " + msg["selectedDate"] + ';'))
                        );
                    }

                    app.showAverageGradeModal();
                });
        },


    };
    app.initialize();
}) ();
function addGrade(form) {
    $.ajax({
        url: $(location).attr("protocol") + "//" + $(location).attr("host") + "/create/grade",
        type: 'POST',
        data: $(form).serialize()
    }).done(function(data){
        $("#statusMessage").html(data["statusMessage"]);
    });
}
