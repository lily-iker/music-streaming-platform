import { NavLink } from "react-router-dom";
import { Button } from "./ui/button";
import SearchBar from "./SearchBar";
import { useMusicStore } from "@/stores/useMusicStore";
import { useAuthStore } from "@/stores/useAuthStore";

const Topbar = () => {
    const { authUser, isAdmin, logout } = useAuthStore();
    const { clearSearch } = useMusicStore(); 

    const handleLogout = async () => {
        await logout();
        window.location.reload();
    };    

  return (
    <div className="flex items-center justify-between p-4 sticky top-0 bg-zinc-900 backdrop-blur-md z-10 mb-2 rounded-lg">
        <div className="flex gap-2 items-center">
            <SearchBar/>
        </div>
        <div className="flex gap-4 items-center">
            {isAdmin && (
                <NavLink className="flex" to={"/admin"}>
                     <Button 
                        onClick={clearSearch}
                        className="bg-theme-400 text-black hover:bg-theme-500">
                        Admin Dashboard
                     </Button>
                </NavLink>
            )}

            {authUser ? ( 
                <Button className="bg-theme-400 text-black hover:bg-theme-500" onClick={handleLogout}>
                    Sign out
                </Button>
                
            ) : (
                <NavLink to={"/login"}>
                     <Button className="bg-theme-400 text-black hover:bg-theme-500">
                        Sign in
                     </Button>
                </NavLink>
            )}

        </div>
    </div>
  )
}

export default Topbar