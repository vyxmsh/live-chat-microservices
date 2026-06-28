import axios from 'axios';

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL,
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");

    console.log("================================");
    console.log("REQUEST:", config.url);
    console.log("TOKEN:", token);

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
        console.log("AUTH HEADER:", config.headers.Authorization);
    }

    console.log("================================");

    return config;
});


api.interceptors.response.use(
    (response) => response,
    (error) => {
        console.log("========== RESPONSE ERROR ==========");
        console.log("URL:", error.config?.url);
        console.log("METHOD:", error.config?.method);
        console.log("STATUS:", error.response?.status);
        console.log("DATA:", error.response?.data);
        console.log("===================================");

        return Promise.reject(error);
    }
);

export default api;