import { Music } from "lucide-react";
import { Link } from "react-router-dom";
import SearchBar from "../SearchBar";
import { useMusicStore } from "@/stores/useMusicStore";

const Header = () => {
	const { clearSearch } = useMusicStore(); 

	return (
		<div className='flex items-center justify-between'>
			<div className='flex items-center gap-4 mb-8'>
				<Link to={"/"}>
					<div
						onClick={clearSearch}
						className="w-12 h-12 bg-theme-400 rounded-xl flex items-center justify-center hover:bg-theme-500
						transition-colors">
						<Music className="w-6 h-6 text-primar" />
					</div>
				</Link>
				<div>
					<h1 className='text-3xl font-bold'>Music Manager</h1>
					<p className='text-zinc-400 mt-1'>Manage your music catalog</p>
				</div>
			</div>
			<SearchBar/>
		</div>
	);
};
export default Header;