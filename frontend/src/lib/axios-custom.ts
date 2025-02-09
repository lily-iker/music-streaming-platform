import axios from "axios";

const baseURL = import.meta.env.VITE_BACKEND_URL;

const instance = axios.create(
    {
        baseURL: baseURL,
        withCredentials: true
    }
)

export function getCookie(name: string) {
  const value = "; " + document.cookie;
  const parts = value.split("; " + name + "=");
  
  if (parts.length == 2) {
      const cookiePart = parts.pop();
      return cookiePart ? cookiePart.split(";").shift() : `Cookie with name: ${name} not found`;
  }
}

// Flag to track if a refresh attempt has been made
let isRefreshing = false;

// Add a request interceptor
instance.interceptors.request.use(async function (config) {
    const accessToken = getCookie('accessToken');
    if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
        isRefreshing = false;
    } 
    else if (!isRefreshing) {

        isRefreshing = true;

        // Attempt to refresh the access token
        try {
            await axios.post(`${baseURL}/api/auth/refresh`, {}, {
                withCredentials: true
            });
            const newAccessToken = getCookie('accessToken');
            config.headers['Authorization'] = `Bearer ${newAccessToken}`;
        } catch (error) {
            // Do something with response error
        }
    }

    return config;
}, function (error) {
    return Promise.reject(error);
});

// Add a response interceptor
instance.interceptors.response.use(function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    return response;
  }, function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    console.log(error)
    return Promise.reject(error);
  });

export default instance;