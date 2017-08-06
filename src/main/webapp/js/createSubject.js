// Wait for the DOM to be ready
$(function() {
    // Initialize form validation on the registration form.
    // It has the name attribute "registration"
    jQuery.validator.addMethod("noSpace", function(value, element) {
        return value.indexOf(" ") < 0 && value != "";
    }, "Space are not allowed");
    $("form[name='addSubject']").validate({
        // Specify validation rules
        rules: {
            // The key name on the left side is the name attribute
            // of an input field. Validation rules are defined
            // on the right side
            subjectTitle: {
                required : true,
                noSpace: true,
                maxlength: 32
            }
        },
        // Specify validation error messages
        messages: {
            subjectTitle:{
                required : "Enter subject's title",
                maxlength: "32 characters max"
            }
        },
        // Make sure the form is submitted to the destination defined
        // in the "action" attribute of the form when valid
        submitHandler: function(form) {
            form.submit();
        }
    });
});
