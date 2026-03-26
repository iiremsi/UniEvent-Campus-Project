import axios from 'axios';

const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const authAPI = {
    register: (data) => 
        axios.post(`${API_URL}/auth/register`, data , {withCredentials: true}),
    
    login: (data) => 
        axios.post(`${API_URL}/auth/login`, data , {withCredentials: true})
};


const api = axios.create({
    baseURL: API_URL,
    withCredentials: true
});

// Her istekte token ekle
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    console.log('🔑 Token localStorage:', token);  // ← DEBUG
    
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
        console.log('✅ Authorization header eklendi');
    } else {
        console.warn('⚠️ Token bulunamadı!');
    }
    return config;
});

export const postAPI = {
    getAll: (page = 0, size = 20) => 
        api.get(`/posts?page=${page}&size=${size}`),
    
    create: (data) => 
        api.post('/posts', data),
    
    delete: (id) => 
        api.delete(`/posts/${id}`),

    like: (postId) => 
        api.post(`/posts/${postId}/like`),
    
    unlike: (postId) => 
        api.delete(`/posts/${postId}/like`),

    getUserPosts: (userId) => 
        api.get(`/posts/user/${userId}`)
};

export const userAPI = {
    getUserProfile: (userId) =>
        api.get(`/api/users/${userId}`),
};



