<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="ar.edu.ubp.das.i18n.text" />

<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-md-4 col-md-offset-7">
            <h3>
                <fmt:message key="login.label.title"/>
            </h3>
            <form>
                <div class="form-group">
                    <label>
                        <fmt:message key="login.label.username"/>
                    </label>
                    <input type="text" class="form-control" placeholder="<fmt:message key="login.placeholder.username"/>" ng-model="person.login">
                </div>
                <div class="form-group">
                    <label>
                        <fmt:message key="login.label.password"/>
                    </label>
                    <input type="password" class="form-control" placeholder="<fmt:message key="login.placeholder.password"/>" ng-model="person.password">
                </div>
                <button type="submit" class="btn btn-success" ng-click="CommandManager.execute('register',person)">
                    <fmt:message key="login.button.login"/>
                </button>
            </form>
        </div>
    </div>
</div>
