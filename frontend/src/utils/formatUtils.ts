export const formatDuration = (seconds: number) => {
	const minutes = Math.floor(seconds / 60);
	const remainingSeconds = seconds % 60;
	return `${minutes}:${remainingSeconds.toString().padStart(2, "0")}`;
};

export const formatLikeCount = (count: number) => {
    return count.toString().replace(/\B(?=(\d{3})+(?!\d))/g, "."); // Format with dots
};