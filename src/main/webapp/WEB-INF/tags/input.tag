<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag display-name="input" pageEncoding="UTF-8" body-content="empty" %>

<%@attribute name="name" description="Input's name and ID." required="true" type="java.lang.String" %>
<%@attribute name="type" description="Input's name and ID." type="java.lang.String" %>
<%@attribute name="placeholder" description="Input's name and ID." type="java.lang.String" %>
<%@attribute name="required" description="Is required?" type="java.lang.Boolean" %>
<%@attribute name="withValidity" description="Add a validation field?" type="java.lang.Boolean" %>
<%@attribute name="validityMSG" description="The invalidity message." type="java.lang.String" %>
<%@attribute name="length" description="String length." type="java.lang.Integer" %>

<div class="mb-4" id="${name}Div">
	<div class="form-group row">
		<div class="col-sm-10">
			<input type="${ not empty type? type : 'text' }" class="form-control col-sm-12"
			       id="${name}" name="${name}"
			       placeholder="${placeholder}"
			       maxlength="${ not empty length ? length : 30 }"
				<c:out value="${ required? 'required' : '' }"/>
			/>
			<c:if test="${ withValidity }">
				<div class="invalid-feedback">
					${ validityMSG }
				</div>
			</c:if>
		</div>
	</div>
</div>