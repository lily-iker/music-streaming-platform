import { Navigate, Route, Routes } from "react-router-dom"
import HomePage from "./pages/HomePage"
import { useAuthStore } from "./stores/useAuthStore";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import { useState } from "react";
import { useEffect } from "react";
import MainLayout from "./layout/MainLayout";
import AlbumPage from "./pages/AlbumPage";
import ArtistPage from "./pages/ArtistPage";
import SongPage from "./pages/SongPage";
import SearchPage from "./pages/SearchPage";
import AdminPage from "./pages/AdminPage";
import Loading from "./components/Loading";
import { Toaster } from "react-hot-toast";
import NotFoundPage from "./pages/NotFoundPage";

function App() {

  const { authUser, fetchAuthUser } = useAuthStore();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchAuthUser();
  }, []);

  useEffect(() => {
    // Simulate a loading delay
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 500); // 0.5 seconds delay

    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return (<Loading/>);
  }

  return (
   <>
    <Routes>
      <Route element={<MainLayout/>}>
       <Route path="/" element={<HomePage/>}/>
       <Route path="/album/:albumId" element={<AlbumPage/>}/>
       <Route path="/artist/:artistId" element={<ArtistPage/>} />
       <Route path="/song/:songId" element={<SongPage/>} />
       <Route path="/search/" element={<SearchPage/>} />
       <Route path="*" element={<NotFoundPage/>} />
      </Route>
      <Route path="/login" element={!authUser ? <LoginPage/> : <Navigate to={"/"}/>}/>
      <Route path="/signup" element={!authUser ? <SignUpPage/> : <Navigate to={"/"}/>}/>
      <Route path="/admin" element={<AdminPage/>}/>      
    </Routes>
    <Toaster/>
   </>
  )
}

export default App
