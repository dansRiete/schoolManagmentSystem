/**
 * Created by Aleks on 01.08.2017.
 */
$(document).ready(
    $('#modalAddGrade').on('hidden.bs.modal', function () {
        window.location.reload(true);
    })
);
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
            $("#statusMessage").html("");
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
                url: link,
                type: "POST",
                data: form.serialize()
            })
                .fail(function () {
                    console.log("Sending form failed")
                })
                .done(function (msg) {
                    if(msg["avg"] !== null && msg["avg"] !== undefined && msg["avg"] !== ''){
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
        }
    };
    app.initialize();
}) ();


function addGrade(form) {

    $("#statusMessage").html("");

    let date = $("#date").val();
    let subjectId = $("#selectedSubject").val();
    let mark = $("#mark").val();
    console.log('date=' + date);
    console.log('subjectId=' + subjectId);
    console.log('mark=' + mark);

    if(date === null || date === undefined || date === ''){
        $("#statusMessage").html("Select a date");
    }else if(subjectId === null || subjectId === undefined || subjectId === ''){
        $("#statusMessage").html("Select a subject");
    }else if(mark === null || mark === undefined || mark === ''){
        $("#statusMessage").html("Enter a mark");
    }else if(mark < 0){
        $("#statusMessage").html("Mark can not be less than zero");
    }else {
        $.ajax({
            url: $(location).attr("protocol") + "//" + $(location).attr("host") + "/create/grade",
            type: 'POST',
            data: $(form).serialize()
        }).done(function(data){
            let p = $("#statusMessage");
            p.attr("class", "alert-success");
            p.html(data["statusMessage"]);
        });
    }
}
