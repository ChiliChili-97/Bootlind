$(document).ready(function () {
    // 쿠키에서 인증 토큰 가져오기
    const token = Cookies.get('Authorization');

    // 로그아웃 버튼 클릭 시 쿠키 삭제 및 로그인 페이지로 이동
    $('#logout-btn').on('click', function () {
        Cookies.remove('Authorization', {path: '/'});
        window.location.href = '/login.html';
    });

    $('#user-btn').on('click', function () {
        getUserInfo();
    });

    function getUserInfo() {
        $.ajax({
            type: "GET",
            url: "/users/getuserinfo",
            headers: {
                Authorization: token
            },
            success: function (user) {
                displayUserInfo(user);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("사용자 정보를 가져오는데 실패했습니다.");
            }
        });
    }

    function displayUserInfo(user) {
        $('#post-list').empty();
        $('#comment-list').empty(); // 기존의 댓글 목록을 지움
        const userInfo = `
        <h2>사용자 정보<button id="activate-btn">활성화</button><button id="deactivate-btn">비활성화</button><button id="delete-user-btn">회원탈퇴</button></h2>
        <p>ID: ${user.username}<button id="update-username-btn">수정하기</button></p>
        <p>닉네임: ${user.nickname}<button id="update-nickname-btn">수정하기</button></p>
        <p>프로필: ${user.profile}<button id="update-profile-btn">수정하기</button></p>
        <p>역할: ${user.role}</p>
        <p>상태: ${user.status}</p>
        <p>팔로우 한 유저 목록: ${user.followers.join(', ')}</p>
        <p><button id="update-password-btn">비밀번호 변경</button></p>
        `;
        $('#post-list').empty().append(userInfo);
        $('#activate-btn').on('click', function () {
            activateUser();
        });
        $('#deactivate-btn').on('click', function () {
            deactivateUser();
        });
        $('#delete-user-btn').on('click', function () {
            deleteUser();
        });
        $('#update-username-btn').on('click', function () {
            const postForm = `
    <form id="username-update-form">
        <label for="update-username">새로운 ID</label><br>
        <input type="text" id="update-username" name="update-username"><br>
        <button type="submit">수정 완료</button>
    </form>`;
            // 게시글 작성 폼을 표시할 위치에 추가
            $('#post-list').append(postForm);
            // 게시글 작성 폼이 제출되면 submitPost 함수 호출
            $('#username-update-form').on('submit', function (event) {
                event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
                const newUsername = $('#update-username').val();
                if (newUsername.trim() !== '') {
                    const UpdateData = {
                        username: newUsername
                    };
                    updateUsername(UpdateData);
                }
            });
        });
        $('#update-nickname-btn').on('click', function () {
            const postForm = `
    <form id="nickname-update-form">
        <label for="update-nickname">새로운 nickname</label><br>
        <input type="text" id="update-nickname" name="update-nickname"><br>
        <button type="submit">수정 완료</button>
    </form>`;
            // 게시글 작성 폼을 표시할 위치에 추가
            $('#post-list').append(postForm);
            // 게시글 작성 폼이 제출되면 submitPost 함수 호출
            $('#nickname-update-form').on('submit', function (event) {
                event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
                const newNickname = $('#update-nickname').val();
                if (newNickname.trim() !== '') {
                    const UpdateData = {
                        nickname: newNickname
                    };
                    updateNickname(UpdateData);
                }
            });
        });
        $('#update-profile-btn').on('click', function () {
            const postForm = `
    <form id="profile-update-form">
        <label for="update-profile">새로운 profile</label><br>
        <input type="text" id="update-profile" name="update-profile"><br>
        <button type="submit">수정 완료</button>
    </form>`;
            // 게시글 작성 폼을 표시할 위치에 추가
            $('#post-list').append(postForm);
            // 게시글 작성 폼이 제출되면 submitPost 함수 호출
            $('#profile-update-form').on('submit', function (event) {
                event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
                const newProfile = $('#update-profile').val();
                if (newProfile.trim() !== '') {
                    const UpdateData = {
                        profile: newProfile
                    };
                    updateProfile(UpdateData);
                }
            });
        });
        $('#update-password-btn').on('click', function () {
            const postForm = `
    <form id="password-update-form">
        <label for="old-password">기존 password</label><br>
        <input type="password" id="old-password" name="old-password"><br>
        <label for="new-password">새로운 password</label><br>
        <input type="password" id="new-password" name="new-password"><br>
        <button type="submit">수정 완료</button>
    </form>`;
            // 게시글 작성 폼을 표시할 위치에 추가
            $('#post-list').append(postForm);
            // 게시글 작성 폼이 제출되면 submitPost 함수 호출
            $('#password-update-form').on('submit', function (event) {
                event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
                    const UpdateData = {
                        oldpassword: $('#old-password').val(),
                        newpassword: $('#new-password').val()
                    };
                    updatePassword(UpdateData);
            });
        });
    }

    function updateUsername(UpdateData) {
        $.ajax({
            type: "PUT",
            url: "/users/updates/username",
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(UpdateData),
            success: function (response) {
                alert(response);
                Cookies.remove('Authorization', {path: '/'});
                window.location.href = '/login.html';
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("ID 변경에 실패했습니다.");
            }
        });
    }

    function updateNickname(UpdateData) {
        $.ajax({
            type: "PUT",
            url: "/users/updates/nickname",
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(UpdateData),
            success: function (response) {
                alert(response);
                getUserInfo();
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("닉네임 변경에 실패했습니다.");
            }
        });
    }

    function updateProfile(UpdateData) {
        $.ajax({
            type: "PUT",
            url: "/users/updates/profile",
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(UpdateData),
            success: function (response) {
                alert(response);
                getUserInfo();
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("프로필 변경에 실패했습니다.");
            }
        });
    }

    function updatePassword(UpdateData) {
        $.ajax({
            type: "PUT",
            url: "/users/updates/password",
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(UpdateData),
            success: function (response) {
                alert(response);
                Cookies.remove('Authorization', {path: '/'});
                window.location.href = '/login.html';
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("비밀번호 변경에 실패했습니다.");
            }
        });
    }

    function activateUser() {
        // 게시글 목록 가져오는 AJAX 요청
        $.ajax({
            type: "PUT",
            url: "/users/updates/activate",
            headers: {
                Authorization: token
            },
            success: function (response) {
                alert(response);
                Cookies.remove('Authorization', {path: '/'});
                window.location.href = '/login.html';
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("활성화에 실패했습니다.");
            }
        });
    }


    function deactivateUser() {
        // 게시글 목록 가져오는 AJAX 요청
        $.ajax({
            type: "PUT",
            url: "/users/updates/deactivate",
            headers: {
                Authorization: token
            },
            success: function (response) {
                alert(response);
                Cookies.remove('Authorization', {path: '/'});
                window.location.href = '/login.html';
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("비활성화에 실패했습니다.");
            }
        });
    }

    function deleteUser() {
        // 게시글 목록 가져오는 AJAX 요청
        $.ajax({
            type: "PUT",
            url: "/users/updates/delete",
            headers: {
                Authorization: token
            },
            success: function (response) {
                alert(response);
                Cookies.remove('Authorization', {path: '/'});
                window.location.href = '/login.html';
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("탈퇴에 실패했습니다.");
            }
        });
    }


    // 검색 버튼 클릭 시 검색어를 이용하여 게시글 검색
    $('#search-btn').on('click', function () {
        const searchInput = $('#search-input').val();
        const searchOption = $('#search-option').val();
        if (searchInput.trim() !== '') {
            switch (searchOption) {
                case 'title':
                    searchPostByTitle(searchInput);
                    break;
                case 'category':
                    searchPostByCategory(searchInput);
                    break;
                case 'nickname':
                    searchPostByNickname(searchInput);
                    break;
                default :
                    getPostList();
            }
        }

    });

    // 전체 버튼 클릭 시 게시글 목록 가져오기
    $('#total-btn').on('click', function () {
        getPostList();
    });


    $('#follower-btn').on('click', function () {
        getPostListByFollower();
    });

    // 페이지 로드 시 게시글 목록 가져오기
    getPostList();

    function getPostList() {
        // 게시글 목록 가져오는 AJAX 요청
        $.ajax({
            type: "GET",
            url: "/posts",
            headers: {
                Authorization: token
            },
            success: function (response) {
                displayPosts(response);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글 목록을 불러오는데 실패했습니다.");
            }
        });
    }

    function getPostListByFollower() {
        // 게시글 목록 가져오는 AJAX 요청
        $.ajax({
            type: "GET",
            url: "/posts/folllowers",
            headers: {
                Authorization: token
            },
            success: function (response) {
                displayPosts(response);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("팔로워 게시글 목록을 불러오는데 실패했습니다.");
            }
        });
    }

    // 게시글을 검색하는 함수
    function searchPostByTitle(title) {
        $.ajax({
            type: "GET",
            url: "/posts/titles/all/" + title,
            headers: {
                Authorization: token
            },
            success: function (response) {
                displayPosts(response);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글을 검색하는데 실패했습니다.");
            }
        });
    }

    function searchPostByCategory(category) {
        $.ajax({
            type: "GET",
            url: "/posts/categories/" + category,
            headers: {
                Authorization: token
            },
            success: function (response) {
                displayPosts(response);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글을 검색하는데 실패했습니다.");
            }
        });
    }

    function searchPostByNickname(nickname) {
        $.ajax({
            type: "GET",
            url: "/posts/users/" + nickname,
            headers: {
                Authorization: token
            },
            success: function (response) {
                displayPosts(response);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글을 검색하는데 실패했습니다.");
            }
        });
    }

    // 게시글을 화면에 출력하는 함수
    function displayPosts(posts) {
        $('#post-list').empty();
        $('#comment-list').empty(); // 기존의 댓글 목록을 지움
        posts.forEach(function (post) {
            const card = `
            <li class="card" data-post-id="${post.id}">
                <h1>${post.title}</h1>
                <h3>${post.category}</h3>
                <h3>${post.id}</h3>
                <p>작성자: ${post.nickname}</p>
                <p>${post.content}</p>
                <p>좋아요: ${post.likescnt}</p>
            </li>`;
            $('#post-list').append(card);
        });

        // 각 게시글에 클릭 이벤트 추가
        $('.card').on('click', function () {
            const postId = $(this).data('post-id');
            getPostById(postId);
        });
    }

// 특정 게시글의 상세 정보를 가져와서 출력하는 함수
    function getPostById(postId) {
        $.ajax({
            type: "GET",
            url: "/posts/" + postId,
            headers: {
                Authorization: token
            },
            success: function (post) {
                displayPostDetail(post);
                getComments(postId); // 상세 정보를 가져온 후에 댓글을 가져옴
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글을 가져오는데 실패했습니다.");
            }
        });
    }


    // 특정 게시글의 상세 정보를 화면에 출력하는 함수
    function displayPostDetail(post) {
        $('#post-list').empty();
        const card = `
    <li class="card">
        <p><button id="update-post-btn">게시글 수정</button></p>
        <p><button id="delete-post-btn">게시글 삭제</button></p>
        <h1>${post.title}</h1>
        <h3>카테고리: ${post.category}</h3>
        <h3>게시글 ID : ${post.id}</h3>
        <p>작성자: ${post.nickname}<button id="follow-btn">팔로우</button></p>
        <p>${post.content}</p>
        <p>좋아요: <span id="like-count">${post.likescnt}</span> <button id="like-btn">좋아요</button></p>
    </li>`;
        $('#post-list').append(card);

        // 게시글 작성 버튼 클릭 시 게시글 작성 폼을 표시
        $('#update-post-btn').on('click', function () {
            const postNum = post.id;
            // 게시글 작성 폼을 보여줄 HTML을 작성
            const postForm = `
    <form id="post-update-form">
        <label for="update-title">제목:</label><br>
        <input type="text" id="update-title" name="update-title"><br>
        <label for="update-category">카테고리:</label><br>
        <input type="text" id="update-category" name="update-category"><br><br>
        <label for="update-content">내용:</label><br>
        <textarea id="update-content" name="update-content"></textarea><br>
        <button type="submit">수정 완료</button>
    </form>`;

            // 게시글 작성 폼을 표시할 위치에 추가
            $('#post-list').prepend(postForm);

            // 게시글 작성 폼이 제출되면 submitPost 함수 호출
            $('#post-update-form').on('submit', function (event) {
                event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
                const postUpdateData = {
                    title: $('#update-title').val(), // 변경된 부분
                    content: $('#update-content').val(),
                    category: $('#update-category').val()
                };
                updatePost(postNum, postUpdateData);
            });
        });

        // 댓글 입력 폼 추가
        const commentForm = `
    <form id="comment-form">
        <label for="comment-content">댓글 작성:</label><br>
        <textarea id="comment-content" name="comment-content"></textarea><br>
        <button type="button" id="submit-comment">댓글 작성</button>
    </form>`;
        $('#post-list').append(commentForm);

        // 댓글 작성 버튼에 클릭 이벤트 핸들러 등록
        $('#submit-comment').on('click', function () {
            const commentContent = $('#comment-content').val();
            const postId = post.id; // 현재 게시글의 ID 가져오기

            if (commentContent.trim() !== '') {
                const commentData = {
                    content: commentContent
                };
                submitComment(postId, commentData);
            }
        });
        $('#like-btn').on('click', function () {
            submitLike(post.id);
        });

        $('#delete-post-btn').on('click', function () {
            deletePost(post.id);
        });

        // 팔로우 버튼에 클릭 이벤트 핸들러 등록
        $('#follow-btn').on('click', function () {
            submitFollow(post.nickname);
        });
    }

    function submitLike(postId) {
        $.ajax({
            type: "POST",
            url: `/posts/likes/` + postId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            success: function (response) {
                alert(response);
                getPostById(postId)
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("좋아요에 실패했습니다.");
            }
        });
    }

    function deletePost(postId) {
        $.ajax({
            type: "DELETE",
            url: `/posts/` + postId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            success: function (response) {
                alert(response);
                getPostList()
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글 삭제에 실패했습니다.");
            }
        });
    }

    function submitFollow(nickname) {
        $.ajax({
            type: "POST",
            url: `/follows/` + nickname,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            success: function (response) {
                alert(response);
                getPostById(postId)
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("팔로우에 실패했습니다.");
            }
        });
    }

    // 특정 게시글의 댓글을 가져오는 함수
    function getComments(postId) {
        $.ajax({
            type: "GET",
            url: "/comments/" + postId,
            headers: {
                Authorization: token
            },
            success: function (response) {
                displayComments(response, postId);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("댓글을 가져오는데 실패했습니다.");
            }
        });
    }

// 댓글을 화면에 출력하는 함수
    function displayComments(comments, postId) {
        $('#comment-list').empty();
        comments.forEach(function (comment) {
            const card = `
        <li class="card" data-comment-id="${comment.id}">
            <p><button class="delete-comment-btn" data-comment-id="${comment.id}">댓글 삭제</button><button class="update-comment-btn" data-comment-id="${comment.id}">댓글 수정</button></p>
            <p>작성자: ${comment.nickname} <button class="follow-btn" data-nickname="${comment.nickname}">팔로우</button></p>
            <p>${comment.content}</p>
            <p>좋아요: <span id="like-count-${comment.id}">${comment.likescnt}</span> <button class="comment-like-btn" data-comment-id="${comment.id}">좋아요</button></p>
            
        </li>`;
            $('#comment-list').append(card);
        });

        $('.comment-like-btn').on('click', function () {
            const commentId = $(this).data('comment-id');
            submitCommentLike(commentId, postId);
        });

        $('.delete-comment-btn').on('click', function () {
            const commentId = $(this).data('comment-id');
            deleteComment(commentId, postId);
        });

        $('.update-comment-btn').on('click', function () {
            const commentId = $(this).data('comment-id');
            const postNum = postId;
            const commentForm = `
    <form id="comment-form">
        <label for="comment-content">댓글 수정:</label><br>
        <textarea id="update-comment-content" name="update-comment-content"></textarea><br>
        <button type="button" id="submit-update-comment">댓글 수정</button>
    </form>`;
            $('#comment-list').append(commentForm);

            // 댓글 수정 버튼에 클릭 이벤트 핸들러 등록
            $('#submit-update-comment').on('click', function () {
                const commentContent = $('#update-comment-content').val();
                const postId = postNum; // 현재 게시글의 ID 가져오기

                if (commentContent.trim() !== '') {
                    const commentData = {
                        content: commentContent
                    };
                    updateComment(postId, commentId, commentData);
                }
            });
        });

        // 댓글 작성자를 팔로우하는 버튼에 클릭 이벤트 핸들러 등록
        $('.follow-btn').on('click', function () {
            const nickname = $(this).data('nickname');
            submitFollow(nickname);
        });
    }

    function submitCommentLike(commentId, postId) {
        $.ajax({
            type: "POST",
            url: `/comments/likes/` + commentId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            success: function (response) {
                alert(response);
                getPostById(postId);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("좋아요에 실패했습니다.");
            }
        });
    }

    function updateComment(postId, commentId, commentData) {
        $.ajax({
            type: "PUT",
            url: `/comments/` + commentId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(commentData),
            success: function (response) {
                alert("댓글이 수정되었습니다.");
                // 댓글 작성 완료 후 댓글 목록 다시 불러오기
                getComments(postId);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("댓글 수정에 실패했습니다.");
            }
        });
    }

    function deleteComment(commentId, postId) {
        $.ajax({
            type: "DELETE",
            url: `/comments/` + commentId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            success: function (response) {
                alert(response);
                getPostById(postId);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("삭제에 실패했습니다.");
            }
        });
    }

// 댓글을 서버에 전송하는 함수
    function submitComment(postId, commentData) {
        $.ajax({
            type: "POST",
            url: `/comments/` + postId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(commentData),
            success: function (response) {
                alert("댓글이 작성되었습니다.");
                // 댓글 작성 완료 후 댓글 목록 다시 불러오기
                getComments(postId);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("댓글 작성에 실패했습니다.");
            }
        });
    }

    // 게시글 작성 버튼 클릭 시 게시글 작성 폼을 표시
    $('#create-post-btn').on('click', function () {
        // 게시글 작성 폼을 보여줄 HTML을 작성
        const postForm = `
    <form id="post-form">
        <label for="create-title">제목:</label><br>
        <input type="text" id="create-title" name="create-title"><br>
        <label for="create-category">카테고리:</label><br>
        <input type="text" id="create-category" name="create-category"><br><br>
        <label for="content">내용:</label><br>
        <textarea id="content" name="content"></textarea><br>
        <button type="submit">작성 완료</button>
    </form>`;

        // 게시글 작성 폼을 표시할 위치에 추가
        $('#post-list').prepend(postForm);

        // 게시글 작성 폼이 제출되면 submitPost 함수 호출
        $('#post-form').on('submit', function (event) {
            event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
            const postData = {
                title: $('#create-title').val(), // 변경된 부분
                content: $('#content').val(),
                category: $('#create-category').val()
            };
            submitPost(postData);
        });
    });


    // 게시글 작성 요청을 서버에 전송하는 함수
    function submitPost(postData) {
        $.ajax({
            type: "POST",
            url: "/posts",
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(postData),
            success: function (response) {
                alert("게시글이 작성되었습니다.");
                // 작성 완료 후 게시글 목록 다시 불러오기
                getPostList();
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글 작성에 실패했습니다.");
            }
        });
    }

    // 게시글 작성 요청을 서버에 전송하는 함수
    function updatePost(postId, postUpdateData) {
        $.ajax({
            type: "PUT",
            url: "/posts/" + postId,
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(postUpdateData),
            success: function (response) {
                alert("게시글이 수정되었습니다.");
                // 작성 완료 후 게시글 목록 다시 불러오기
                getPostById(postId);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("게시글 수정에 실패했습니다.");
                getPostById(postId);
            }
        });
    }

    // 게시글 작성 버튼 클릭 시 게시글 작성 폼을 표시
    $('#create-category-btn').on('click', function () {
        // 게시글 작성 폼을 보여줄 HTML을 작성
        const postForm = `
    <form id="category-form">
        <label for="create-category">카테고리:</label><br>
        <input type="text" id="create-category" name="create-category"><br><br>
        <button type="submit">추가하기</button>
    </form>`;

        // 게시글 작성 폼을 표시할 위치에 추가
        $('#post-list').prepend(postForm);

        // 게시글 작성 폼이 제출되면 submitPost 함수 호출
        $('#category-form').on('submit', function (event) {
            event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
            const categoryData = {
                category: $('#create-category').val()
            };
            submitCategory(categoryData);
        });
    });

    function submitCategory(categoryData) {
        $.ajax({
            type: "POST",
            url: "/categories",
            headers: {
                Authorization: token
            },
            contentType: 'application/json',
            data: JSON.stringify(categoryData),
            success: function (response) {
                alert(response);
                // 작성 완료 후 게시글 목록 다시 불러오기
                getPostList();
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("카테고리가 추가에 실패했습니다.");
            }
        });
    }
});
