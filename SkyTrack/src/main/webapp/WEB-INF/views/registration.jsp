<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User</title>
	<link href="static/css/adminpage.css" rel="stylesheet" media="screen">
    <link href="static/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="static/css/formValidation.min.css" rel="stylesheet" media="screen">
    <link href="static/css/datepicker.min.css" rel="stylesheet" media="screen">
    <link href="static/css/datepicker3.min.css" rel="stylesheet" media="screen">

</head>

<body style="margin: 0; height: 100%; overflow: hidden ">

    <div id="wrapper" style="max-height: 100vh;overflow-y:none;overflow-x:none">
  <jsp:include page="includeHeaders.jsp" />  
    <jsp:include page="includeTabs.jsp" />  
  <div class="tab-content">
    <div id="admin" class="tab-pane fade in active" >  
      <jsp:include page="Include_Admin_Sidemenu.jsp" />  
       
          <!-- Page Content -->
        <div id="page-wrapper" style="max-height: 100vh;overflow-y:none;overflow-x:none">
            <div class="container-fluid" style="padding-right:0px;padding-left:0px;">
                <div class="row-fluid"> 
				 <div class="col-lg-12" style="max-height: 90vh; padding-right:0px;padding-left:0px; overflow-y:scroll;overflow-x:scroll;" >
  		  	<div id="containerPage" class="row-fluid">
			 <div align="center"><h4 class="page-title">User</h4></div>
	 	<form:form method="POST" modelAttribute="user" class="form-horizontal">
			<form:input type="hidden" path="id" id="id"/>
			<div class="well">
			<div class="row">
				<div class="form-group col-md-6">
					<label class="col-md-3 control-lable" for="firstName">First Name</label>
					<div class="col-md-7">
						<form:input type="text" path="firstName" id="firstName" class="form-control input-sm"/>
						<div class="has-error">
							<form:errors path="firstName" class="help-inline"/>
						</div>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="form-group col-md-6">
					<label class="col-md-3 control-lable" for="lastName">Last Name</label>
					<div class="col-md-7">
						<form:input type="text" path="lastName" id="lastName" class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="lastName" class="help-inline"/>
						</div>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="form-group col-md-6">
					<label class="col-md-3 control-lable" for="ssoId">SSO ID</label>
					<div class="col-md-7">
						<c:choose>
							<c:when test="${edit}">
								<form:input type="text" path="ssoId" id="ssoId" class="form-control input-sm" disabled="true"/>
							</c:when>
							<c:otherwise>
								<form:input type="text" path="ssoId" id="ssoId" class="form-control input-sm" />
								<div class="has-error">
									<form:errors path="ssoId" class="help-inline"/>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="form-group col-md-6">
					<label class="col-md-3 control-lable" for="password">Password</label>
					<div class="col-md-7">
						<form:input type="password" path="password" id="password" class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="password" class="help-inline"/>
						</div>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="form-group col-md-6">
					<label class="col-md-3 control-lable" for="email">Email</label>
					<div class="col-md-7">
						<form:input type="text" path="email" id="email" class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="email" class="help-inline"/>
						</div>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="form-group col-md-6">
					<label class="col-md-3 control-lable" for="userProfiles">Roles</label>
					<div class="col-md-7">
						<form:select path="userProfiles" items="${roles}" multiple="true" itemValue="id" itemLabel="type" class="form-control input-sm" />
						<div class="has-error">
							<form:errors path="userProfiles" class="help-inline"/>
						</div>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div align="center">
					<c:choose>
						<c:when test="${edit}">
							<input type="submit" value="Update" class="btn btn-primary btn-sm"/>  <a class="btn btn-danger btn-sm" role="button" href="< c:url value='/list' />">Cancel</a>
						</c:when>
						<c:otherwise>
							<input type="submit" value="Save" class="btn btn-primary btn-sm"/>  <a class="btn btn-danger btn-sm" role="button" href="<c:url value='/list' />">Cancel</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			</div>
		</form:form>
	</div>
	</div>
	</div>
	</div>
	</div>
	
			</div>	

		<div id="dashboard" class="tab-pane fade">
      <h3>Dashboard 1</h3>
      
    </div>
    <div id="customer" class="tab-pane fade">
   <h3>Track</h3>
   
    </div>
    <div id="report" class="tab-pane fade">
     <h3>Report</h3>
       </div>

			
   
				</div>
					</div>
</body>
</html>