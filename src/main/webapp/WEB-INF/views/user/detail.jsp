<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<div class="container pt-header" id="app" v-cloak="true">
    <div>
        <p>닉네임 : {{user.nickname}}</p>
        <p>등급 : {{user.authority}}</p>
    </div>
</div>
<script type="module">
    import { createApp, ref, computed } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    createApp({
        setup() {
            let user=ref([]);
            async function getUser(){
                try{
                    let res=await axios({
                        method:'post',
                        url:'${pageContext.request.contextPath}/api${requestScope['javax.servlet.forward.servlet_path']}',
                        headers:{
                            "Content-Type":"application/json"
                        },
                        withCredentials: true
                    });
                    if(res.status === 404){
                        console.log(res.data.message);
                    }else{
                        console.log(res.data.data);
                        user.value=res.data.data;
                    }
                }catch (e) {
                    console.log(e)
                }
            }

            return {
                getUser, user
            }
        },
        mounted(){
            this.getUser()
        }
    }).mount('#app')
</script>
<script>
    const m1=newmodal('<b>test</b> test','m1');
</script>
