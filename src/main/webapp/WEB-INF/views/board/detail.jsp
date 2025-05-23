<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="loginUserNo" value="${requestScope.userno}" />
<html>
<head>
    <title>🐾</title>
    <style>
        .detail-container {
            max-width: 800px;
            margin: 30px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background-color: #fffefc;
            box-shadow: 0 1px 6px rgba(0,0,0,0.1);
        }
        .post-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .category-badge {
            display: inline-block;
            background-color: #f5da42;
            color: black;
            font-size: 14px;
            padding: 2px 10px;
            border-radius: 12px;
            margin-bottom: 10px;
        }
        .post-meta {
            font-size: 13px;
            color: #888;
            margin-bottom: 20px;
        }
        .post-image img {
            max-width: 100%;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .post-content {
            font-size: 16px;
            color: #333;
            line-height: 1.6em;
            white-space: pre-line;
        }
        .post-actions {
            margin-top: 30px;
            text-align: right;
        }
        .post-actions a {
            margin-left: 10px;
            text-decoration: none;
            color: #007bff;
        }
        .comment-box {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #f9f9f9;
        }
        textarea {
            width: 100%;
            padding: 0.5rem;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        button {
            margin-top: 0.5rem;
            padding: 0.5rem 1rem;
            background-color: #4f46e5;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .comment-item {
            background: #fff;
            border-radius: 6px;
            padding: 0.5rem;
            margin-bottom: 0.5rem;
        }
        .comment-meta {
            font-size: 0.85rem;
            color: #666;
        }
    </style>
</head>
<body>
<h1>게시글 상세보기</h1>
<div class="detail-container">
    <div class="category-badge">${vo.type}</div>
    <div class="post-title">${vo.title}</div>
    <div class="post-meta">
        작성자: ${vo.nickname} |
        작성일: <fmt:formatDate value="${vo.created_at}" pattern="yyyy-MM-dd" /> |
        조회수: ${vo.views} |
        좋아요: ${vo.like_count}
    </div>

    <c:if test="${not empty vo.image_url}">
        <div class="post-image">
            <img src="${vo.image_url}" alt="게시글 이미지">
        </div>
    </c:if>

    <div class="post-content">
        ${vo.content}
    </div>

    <div class="post-actions">
        <c:if test="${loginUserNo != null && loginUserNo == vo.user_no}">
            <a href="update?post_id=${vo.post_id}">✏ 수정</a>
            <a href="delete?post_id=${vo.post_id}" onclick="return confirm('정말 삭제하시겠습니까?')">🗑 삭제</a>
        </c:if>
        <a href="list">📋 목록</a>
    </div>
</div>

<div id="commentApp">
    <div class="comment-box">
        <h2>댓글</h2>
        <textarea v-model="newComment" placeholder="댓글을 입력하세요..."></textarea>
        <button @click="addComment">등록</button>

        <div v-if="comments.length > 0">
            <div v-for="comment in comments" :key="comment.comment_id" class="comment-item">
                <p><strong>{{ comment.nickname }}</strong>: {{ comment.content }}</p>
                <p class="comment-meta">작성일: {{ new Date(comment.created_at).toLocaleString() }}</p>
                <button @click="deleteComment(comment.comment_id)">삭제</button>
            </div>
        </div>
        <p v-else>작성된 댓글이 없습니다.</p>
    </div>
</div>

<script>
    const USER_NO = ${loginUserNo != null ? loginUserNo : 'null'};
</script>

<script type="module">
    import { createApp, ref, onMounted } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    const commentApp = createApp({
        setup() {
            const comments = ref([])
            const newComment = ref('')
            const postId = ${vo.post_id}

            const fetchComments = async () => {
                try {
                    const res = await fetch(`/comment/list?post_id=${postId}`)
                    comments.value = await res.json()
                } catch (err) {
                    console.error('댓글 불러오기 실패:', err)
                }
            }

            const addComment = async () => {
                if (newComment.value.trim() === '' || USER_NO == null) return

                try {
                    await fetch('/comment/insert', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            post_id: postId,
                            user_no: USER_NO,
                            content: newComment.value
                        })
                    })
                    newComment.value = ''
                    await fetchComments()
                } catch (err) {
                    console.error('댓글 등록 실패:', err)
                }
            }

            const deleteComment = async (comment_id) => {
                try {
                    await fetch(`/comment/delete?comment_id=${comment_id}`, { method: 'DELETE' })
                    await fetchComments()
                } catch (err) {
                    console.error('댓글 삭제 실패:', err)
                }
            }

            onMounted(fetchComments)

            return {
                comments,
                newComment,
                addComment,
                deleteComment
            }
        }
    })

    commentApp.mount('#commentApp')
</script>
</body>
</html>
