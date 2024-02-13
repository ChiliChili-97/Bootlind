$(document).ready(function () {
    // 쿠키에서 인증 토큰 가져오기
    const token = Cookies.get('Authorization');

    // 로그아웃 버튼 클릭 시 쿠키 삭제 및 로그인 페이지로 이동
    $('#logout-btn').on('click', function () {
        Cookies.remove('Authorization', {path: '/'});
        window.location.href = '/login.html';
    });

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
                default :
                    getPostList();
            }
        }

    });

    // 전체 버튼 클릭 시 게시글 목록 가져오기
    $('#total-btn').on('click', function () {
        getPostList();
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

    // 게시글을 화면에 출력하는 함수
    function displayPosts(posts) {
        $('#post-list').empty();
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
            <h1>${post.title}</h1>
            <h3>카테고리: ${post.category}</h3>
            <h3>게시글 ID : ${post.id}</h3>
            <p>작성자: ${post.nickname}</p>
            <p>${post.content}</p>
            <p>좋아요: ${post.likescnt}</p>
        </li>`;
        $('#post-list').append(card);
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
                displayComments(response);
            },
            error: function (xhr, status, error) {
                console.error(error);
                alert("댓글을 가져오는데 실패했습니다.");
            }
        });
    }

// 댓글을 화면에 출력하는 함수
    function displayComments(comments) {
        $('#comment-list').empty();
        comments.forEach(function (comment) {
            const card = `
            <li class="card" data-comment-id="${comment.id}">
                <p>작성자: ${comment.nickname}</p>
                <p>${comment.content}</p>
                <p>좋아요: ${comment.likescnt}</p>
            </li>`;
            $('#comment-list').append(card);
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
});
