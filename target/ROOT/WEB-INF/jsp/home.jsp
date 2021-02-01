<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="util" class="com.liceu.sromerom.utils.ParseUtils"/>
<html>
<head>
    <title>Your Home</title>
    <link href="/resources/css/home.css" rel="stylesheet">
    <%@ include file="parts/header.jsp" %>
</head>
<body>
<%-- ####################  Header menu #################### --%>
<header>
    <nav id="header" class="p5 navbar navbar-expand-lg">
        <a class="navbar-brand" href="#">Notes Management</a>
        <!-- <a class="navbar-brand" href="#"><img src="https://e7.pngegg.com/pngimages/977/1014/png-clipart-sales-goal-buyer-management-chief-executive-sticky-notes-miscellaneous-text.png" alt="logo"></a>-->
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText"
                aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Home <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/create">Create note</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/editProfile">Edit your profile</a>
                </li>
            </ul>
            <form method="POST" action="${pageContext.request.contextPath}/unlogin" class="inline">
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <button type="submit" class="link-button">
                    Logout
                </button>
            </form>

            <span class="navbar-text"
                  style="padding-right: .5rem; padding-left: .5rem;">Welcome ${username}!</span>
        </div>
    </nav>
</header>
<%-- ####################  Search Filter #################### --%>
<div class="container">
    <div class="justify-content-center">
        <div class="">
            <form class="form-inline" action="${pageContext.request.contextPath}/home" method="GET">
                <div class="form-group mb-3 mt-3 mr-1">
                    <select class="form-control" name="typeNote" id="selectTypeNote">
                        <c:choose>
                            <c:when test="${typeNote == 'sharedNotesWithMe'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option selected="true" value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>

                            <c:when test="${typeNote == 'sharedNotesByYou'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option selected="true" value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>

                            <c:when test="${typeNote == 'ownerNotes'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option selected="true" value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>


                            <c:when test="${typeNote == 'titleDESC'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option selected="true" value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>
                            <c:when test="${typeNote == 'titleASC'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option selected="true" value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>
                            <c:when test="${typeNote == 'creationDateDESC'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option selected="true" value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>
                            <c:when test="${typeNote == 'creationDateASC'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option selected="true" value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>
                            <c:when test="${typeNote == 'lastModificationDESC'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option selected="true" value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>
                            <c:when test="${typeNote == 'lastModificationASC'}">
                                <option disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option selected="true" value="lastModificationASC">Filter by last modification ASC</option>
                            </c:when>
                            <c:otherwise>
                                <option selected="true" disabled="disabled">Select a type of note to filter</option>
                                <option value="ownerNotes">Created Notes</option>
                                <option value="sharedNotesWithMe">Shared Notes With You</option>
                                <option value="sharedNotesByYou">Shared Notes by you</option>
                                <option value="titleDESC">Filter by title DESC</option>
                                <option value="titleASC">Filter by title ASC</option>
                                <option value="creationDateDESC">Filter by creation date DESC</option>
                                <option value="creationDateASC">Filter by creation date ASC</option>
                                <option value="lastModificationDESC">Filter by last modification DESC</option>
                                <option value="lastModificationASC">Filter by last modification ASC</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                </div>
                <div class="form-group mb-3 mt-3 mr-1 w-10">
                    <input type="text" class="form-control" placeholder="title" aria-label="Username"
                           aria-describedby="basic-addon1" name="titleFilter">
                </div>
                <div class="form-group mb-3 mt-3 mr-1 w-10">
                    <div class="">
                        <input id="startDate" class="form-control" type="text" name="noteStart" value="${initDate}"/>
                    </div>
                </div>
                <div class="form-group mb-3 mt-3 mr-1 w-10">
                    <div class="">
                        <input id="endDate" class="form-control" type="text" name="noteEnd" value="${endDate}"/>
                    </div>
                </div>
                <!--
                <div class="form-group mb-3 mt-3 mr-1 w-10">
                    <div class="">
                        <input class="form-control" type="datetime-local" id="startDate" name="noteStart"
                               value="${initDate}">
                    </div>
                </div>
                <div class="form-group mb-3 mt-3 mr-1 w-10">
                    <div class="">
                        <input class="form-control" type="datetime-local" id="endDate" name="noteEnd"
                               value="${endDate}">
                    </div>
                </div>
                -->
                <a class="btn btn-secondary" role="button" href="${pageContext.request.contextPath}/home">Restore</a>
                <button class="btn principalButton text-white" type="submit">Search</button>
            </form>
        </div>
    </div>
</div>
<%-- ####################  Container of notes #################### --%>
<section id="containerNotes">
    <div>
        <c:choose>
            <c:when test="${empty notes}">
                <h2>Nothing to display</h2>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${typeNote == 'sharedNotesWithMe'}">
                        <h2>Your notes that have been shared with you</h2>
                    </c:when>
                    <c:when test="${typeNote == 'sharedNotesByYou'}">
                        <h2>Your notes that you have shared</h2>
                    </c:when>
                    <c:when test="${typeNote == 'ownerNotes'}">
                        <h2>Your notes that you have created</h2>
                    </c:when>
                    <c:otherwise>
                        <h2>All notes</h2>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>


    </div>
    <form action="${pageContext.request.contextPath}/delete" method="POST">
        <input type="hidden" name="_csrftoken" value="${csrfToken}">
        <button id="buttonToDelete" type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalDelete">
            Delete Selected Notes
        </button>
        <div id="siteNotes">
            <%-- ####################  Modal to Delete note #################### --%>
            <div class="modal fade" id="modalDelete" tabindex="-1" role="dialog"
                 aria-labelledby="exampleModalCenterTitle"
                 aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="titleDeleteModal">Are you sure to delete this note?</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Go Back</button>
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </div>
                    </div>
                </div>
            </div>
            <c:forEach var="note" items="${notes}">
                <c:choose>
                    <%-- ####################  Notes propies #################### --%>
                    <c:when test="${note.owner.userid == useridSession}">
                        <c:choose>
                            <%-- #################### Notes compartides #################### --%>
                            <c:when test="${note.sharedUsers != null || typeNote == 'sharedNotesByYou'}">
                                <div class="card sharedNotesByYou"
                                     style="width: 18rem; background-color: #53b3cb; color: black">
                                    <input class="checkboxDelete" type="checkbox" name="checkboxDelete"
                                           value="${note.noteid}">
                                    <div class="card-body">
                                        <h5 class="card-title">${note.title}</h5>
                                        <h6 class="card-subtitle mb-2 text-muted">Created By ${note.owner.username}</h6>
                                        <h6>
                                            Shared with
                                            <c:forEach var="userS" items="${note.sharedUsers}">
                                                ${userS.username},
                                            </c:forEach>
                                        </h6>
                                        <p class="card-text"
                                           style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical;overflow: hidden;">${util.cleanBody(note.body)}</p>
                                        <div class="optionsButtons">
                                            <a href="${pageContext.request.contextPath}/edit?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-pencil-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z"></path>
                                                </svg>
                                            </a>
                                            <a class="card-link"
                                               href="${pageContext.request.contextPath}/share?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-share-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M11 2.5a2.5 2.5 0 1 1 .603 1.628l-6.718 3.12a2.499 2.499 0 0 1 0 1.504l6.718 3.12a2.5 2.5 0 1 1-.488.876l-6.718-3.12a2.5 2.5 0 1 1 0-3.256l6.718-3.12A2.5 2.5 0 0 1 11 2.5z"></path>
                                                </svg>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/deleteShare?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-person-x-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm6.146-2.854a.5.5 0 0 1 .708 0L14 6.293l1.146-1.147a.5.5 0 0 1 .708.708L14.707 7l1.147 1.146a.5.5 0 0 1-.708.708L14 7.707l-1.146 1.147a.5.5 0 0 1-.708-.708L13.293 7l-1.147-1.146a.5.5 0 0 1 0-.708z"></path>
                                                </svg>
                                            </a>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/detail?id=${note.noteid}">
                                            <span class="link-spanner"></span>
                                        </a>
                                        <h6 class="card-subtitle mb-2 text-muted dateInfo"
                                            style="font-size: 10px; margin-top: 10px;">Last
                                            Modification ${util.parseDefaultDateTime(note.lastModification)}</h6>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <%-- #################### Notes creades #################### --%>
                                <div class="card ownerNotes"
                                     style="width: 18rem; background-color: #f9c22e; color: black">
                                    <input class="checkboxDelete" type="checkbox" name="checkboxDelete"
                                           value="${note.noteid}">
                                    <div class="card-body">
                                        <h5 class="card-title">${note.title}</h5>
                                        <h6 class="card-subtitle mb-2 text-muted">Created By ${note.owner.username}</h6>
                                        <p class="card-text"
                                           style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical;overflow: hidden;">${util.cleanBody(note.body)}</p>

                                        <div class="optionsButtons">
                                            <a href="${pageContext.request.contextPath}/edit?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-pencil-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z"></path>
                                                </svg>
                                            </a>
                                            <a class="card-link"
                                               href="${pageContext.request.contextPath}/share?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-share-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M11 2.5a2.5 2.5 0 1 1 .603 1.628l-6.718 3.12a2.499 2.499 0 0 1 0 1.504l6.718 3.12a2.5 2.5 0 1 1-.488.876l-6.718-3.12a2.5 2.5 0 1 1 0-3.256l6.718-3.12A2.5 2.5 0 0 1 11 2.5z"></path>
                                                </svg>
                                            </a>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/detail?id=${note.noteid}">
                                            <span class="link-spanner"></span>
                                        </a>

                                        <h6 class="card-subtitle mb-2 text-muted dateInfo"
                                            style="font-size: 10px; margin-top: 10px;">Last
                                            Modification ${util.parseDefaultDateTime(note.lastModification)}</h6>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <%-- #################### Notes compartides amb tu #################### --%>
                        <div class="card sharedNotesWithMe"
                             style="width: 18rem; background-color: #f15946; color: white">
                            <div class="card-body">
                                <h5 class="card-title">${note.title}</h5>
                                <h6 class="card-subtitle mb-2 text-muted">Created By ${note.owner.username}</h6>
                                <p class="card-text"
                                   style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical;overflow: hidden;">${util.cleanBody(note.body)}</p>
                                <div class="optionsButtons"
                                     <c:if test="${note.writeable eq true}">style="background-color: #f9c22e; border: 2px solid black; padding: 8px;" </c:if>>
                                    <c:choose>
                                        <c:when test="${note.writeable eq true}">
                                            <a href="${pageContext.request.contextPath}/edit?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-pencil-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z"></path>
                                                </svg>
                                            </a>
                                            <a class="card-link"
                                               href="${pageContext.request.contextPath}/share?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-share-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M11 2.5a2.5 2.5 0 1 1 .603 1.628l-6.718 3.12a2.499 2.499 0 0 1 0 1.504l6.718 3.12a2.5 2.5 0 1 1-.488.876l-6.718-3.12a2.5 2.5 0 1 1 0-3.256l6.718-3.12A2.5 2.5 0 0 1 11 2.5z"></path>
                                                </svg>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/deleteShare?id=${note.noteid}">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-person-x-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm6.146-2.854a.5.5 0 0 1 .708 0L14 6.293l1.146-1.147a.5.5 0 0 1 .708.708L14.707 7l1.147 1.146a.5.5 0 0 1-.708.708L14 7.707l-1.146 1.147a.5.5 0 0 1-.708-.708L13.293 7l-1.147-1.146a.5.5 0 0 1 0-.708z"></path>
                                                </svg>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a data-toggle="modal" data-target="#modalDeleteShares"
                                               onclick="passNoteId(${note.noteid})">
                                                <svg width="1em" height="1em" viewBox="0 0 16 16"
                                                     class="bi bi-person-x-fill"
                                                     fill="currentColor"
                                                     xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd"
                                                          d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm6.146-2.854a.5.5 0 0 1 .708 0L14 6.293l1.146-1.147a.5.5 0 0 1 .708.708L14.707 7l1.147 1.146a.5.5 0 0 1-.708.708L14 7.707l-1.146 1.147a.5.5 0 0 1-.708-.708L13.293 7l-1.147-1.146a.5.5 0 0 1 0-.708z"></path>
                                                </svg>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <a href="${pageContext.request.contextPath}/detail?id=${note.noteid}">
                                    <span class="link-spanner"></span>
                                </a>
                                <h6 class="card-subtitle mb-2 text-muted dateInfo"
                                    style="font-size: 10px; margin-top: 10px;">Last
                                    Modification ${util.parseDefaultDateTime(note.lastModification)}</h6>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </form>

    <%-- #################### Pagination section #################### --%>
    <nav id="pagination" aria-label="Page navigation example">
        <ul class="pagination">
            <c:if test="${currentPage != 1}">
                <li class="page-item">
                    <a class="page-link principalButton"
                       href="${pageContext.request.contextPath}/home?currentPage=${currentPage-1}&${filterURL}">Previous</a>
                </li>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <li class="page-item active">
                            <a class="page-link principalButton">
                                    ${i}
                                <span class="sr-only">(current)</span>
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link principalButton"
                               href="${pageContext.request.contextPath}/home?currentPage=${i}&${filterURL}">${i}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage lt totalPages}">
                <li class="page-item">
                    <a class="page-link principalButton"
                       href="${pageContext.request.contextPath}/home?currentPage=${currentPage+1}&${filterURL}">Next</a>
                </li>
            </c:if>
        </ul>
    </nav>
    <%-- ####################  Modal to Delete share Note #################### --%>
    <div class="modal fade" id="modalDeleteShares" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
         aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="titleModalDeleteShares">Are you sure to delete this share?</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Go Back</button>
                    <form id="formModal" class="card-link" method="POST" action="/deleteShare">
                        <input type="hidden" name="_csrftoken" value="${csrfToken}">
                        <input id="noteIdHidden" type="hidden" name="noteid" value="">
                        <input id="userHidden" type="hidden" name="users[]" value="${username}">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<%@ include file="parts/footer.jsp" %>

<script>
    $(function () {
        $('input[name="noteStart"]').daterangepicker({
            singleDatePicker: true,
            timePicker: true,
            autoUpdateInput: false,
            minYear: 1901,
            maxYear: parseInt(moment().format('YYYY'), 10),
            locale: {
                format: 'YYYY-MM-DD hh:mm:ss'
            }
        });
        $('input[name="noteEnd"]').daterangepicker({
            singleDatePicker: true,
            timePicker: true,
            autoUpdateInput: false,
            minYear: 1901,
            maxYear: parseInt(moment().format('YYYY'), 10),
            locale: {
                format: 'YYYY-MM-DD HH:mm:ss'
            }
        });
        $('input[name="noteStart"]').on('apply.daterangepicker', function (ev, picker) {
            $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
        });

        $('input[name="noteStart"]').on('cancel.daterangepicker', function (ev, picker) {
            $(this).val('');
        });

        $('input[name="noteEnd"]').on('apply.daterangepicker', function (ev, picker) {
            $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
        });

        $('input[name="noteEnd"]').on('cancel.daterangepicker', function (ev, picker) {
            $(this).val('');
        });
    });

    let checkedStatus = 0;
    const allCheckbox = document.querySelectorAll("input[name = 'checkboxDelete']");
    document.querySelector("#buttonToDelete").style.display = "none";
    for (let i = 0; i < allCheckbox.length; i++) {
        allCheckbox[i].addEventListener("change", function () {
            if (this.checked) {
                checkedStatus++;
                document.querySelector("#buttonToDelete").style.display = "inline-block";
            } else {
                checkedStatus--;
            }

            if (checkedStatus === 0) {
                document.querySelector("#buttonToDelete").style.display = "none";
            }
        });
    }

</script>
</body>
</html>
