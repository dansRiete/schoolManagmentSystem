<%@ page import = "hello.Customer" %>

<jsp:useBean id="customer" class="hello.Customer" scope="session"/>
<jsp:setProperty name="customer" property="*"/>

<jsp:useBean id="engine" class="hello.GreetingEngine" scope="session"/>

<html>
<head><title>HelloCustomer</title></head>
<body bgcolor="lightblue">
<font size=4>

    <form method=post>	<hr><br>
        Customer Name: <input type=text name=name value=<%= customer.getName() %>> <p>
            Customer Gender:
            <input value="Female" type="radio" name=gender
                    <% if (customer.getGender().equals("Female"))
                        {%> checked="true" <%}%>
            />Female
            <input value="Male" type="radio" name=gender
                    <% if (customer.getGender().equals("Male")) {%> checked="true" <%}%>
            />Male
        <p>
            Customer Age: <input type=int name=age value=<%= customer.getAge() %> > <p>

            Customer Marital Status:
            <input value="Single" type="radio" name=maritalStatus
                    <% if (customer.getMaritalStatus().equals("Single")) {%> checked="true" <%}%>
            />Single
            <input value="Married" type="radio" name=maritalStatus
                    <% if (customer.getMaritalStatus().equals("Married")) {%> checked="true" <%}%>
            />Married
        <p>

            <input type=submit value="Generate Greeting">

    </form>  <p>

    Generated Greeting:

    <b><%= engine.generateGreeting(customer) %></b>

    <br><hr>

</font>
</body>
</html>