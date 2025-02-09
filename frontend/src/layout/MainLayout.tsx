import LeftSidebar from "@/components/LeftSidebar";
import { PlaybackControls } from "@/components/PlaybackControls";
import Player from "@/components/Player";
import Topbar from "@/components/Topbar";
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "@/components/ui/resizable";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
    const isMobile = false;
    return (
        <div className="h-screen bg-black text-white flex flex-col">
            <ResizablePanelGroup direction="horizontal" className="flex flex-1 h-full overflow-hidden p-2">
                <Player/>
                
                {/* Left sidebar */}
                <ResizablePanel defaultSize={25} minSize={isMobile ? 0 : 20} maxSize={30}>
                    <LeftSidebar/>
                </ResizablePanel>

                <ResizableHandle className='w-2 bg-black rounded-lg transition-colors' />
                
                {/* Main content */}
                <ResizablePanel defaultSize={isMobile ? 100 : 75}>
                    <Topbar/>
                    <Outlet/>
                </ResizablePanel>
            </ResizablePanelGroup>
            <PlaybackControls />

        </div>
    )
}

export default MainLayout