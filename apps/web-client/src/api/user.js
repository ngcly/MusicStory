const api = [
    {
        name: "login",
        url: "/signin?client_id=music_story&client_secret=secret",
        method: "post"
    },
    {
        name: "relogin",
        url: "/signin",
        method: "get"
    },
    {
        name: "signup",
        url: "/signup",
        method: "post"
    },
    {
        name: "logout",
        url: "/signout",
        method: "delete"
    },
    {
        name: "userInfo",
        url: "/user/info",
        method: "get"
    },
    {
        name: "updateUser",
        url: "/user/info",
        method: "put"
    },
    {
        name: "addUser",
        url: "/user/info",
        method: "post"
    }
]

export default api;