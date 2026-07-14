import request from "../utils/request";
import user from "./user";

const commonApi = [
    {
        name: "notice",
        url: "/notice",
        method: "get"
    },
    {
        name: "carousel",
        url: "/carousel",
        method: "get"
    },
    {
        name: "essays",
        url: "/essay",
        method: "get"
    },
    {
        name: "readEssay",
        url: "/essay",
        method: "put"
    },
    {
        name: "create",
        url: "/user/essay",
        method: "post"
    },
    {
        name: "essaySearch",
        url: "/search",
        method: "get"
    },
    {
        name: "myessay",
        url: "/user/essay",
        method: "get"
    },
    {
        name: "altessay",
        url: "/user/essay",
        method: "put"
    },
    {
        name: "comments",
        url: "/comments",
        method: "get"
    },
    {
        name: "classify",
        url: "/classify",
        method: "get"
    },
    {
        name: "upload",
        url: "/user/upload",
        method: "post",
        headers: { 'Content-Type': 'multipart/form-data' }
    }
]

const all = commonApi.concat(user);

export default request(all);