import { useState } from "react";
import { Link } from "react-router-dom";
import { Eye, EyeOff, Loader2, Lock, Mail, Music } from "lucide-react";
import { useAuthStore } from "@/stores/useAuthStore";


const LoginPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  
  const { isLoading, login } = useAuthStore();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await login(formData.username, formData.password);
  };

  return (
    <div className="p-4 h-screen flex items-center justify-center bg-gradient-to-b from-zinc-950 to-zinc-800">
      <div className="flex flex-col justify-center items-center p-6 sm:p-12 bg-gradient-to-b from-zinc-800 to-zinc-900 rounded-md">
        <div className="w-full max-w-md space-y-8 px-8">
          {/* Logo */}
          <div className="text-center mb-8">
            <div className="flex flex-col items-center gap-2 group">
              <Link to={"/"}>
                <div
                  className="w-12 h-12 bg-theme-400 rounded-xl flex items-center justify-center hover:bg-theme-500
                  transition-colors">
                  <Music className="w-6 h-6 text-primar" />
                </div>
              </Link>
              <h1 className="text-2xl font-bold mt-2">Welcome Back</h1>
              <p className="text-base-content/60">Sign in to your account</p>
            </div>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit} className="space-y-8">
            <div className="form-control">
              <label className="label">
                <span className="label-text font-medium">Email</span>
              </label>
              <div className="relative mt-2">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Mail className="h-5 w-5 text-base-content/40 text-theme-400" />
                </div>
                <input
                  type="text"
                  className={`input input-bordered w-full pl-10 rounded-md text-theme-400 p-2`}
                  placeholder="you@example.com"
                  value={formData.username}
                  onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                />
              </div>
            </div>

            <div className="form-control">
              <label className="label">
                <span className="label-text font-medium">Password</span>
              </label>
              <div className="relative mt-2">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Lock className="h-5 w-5 text-base-content/40 text-theme-400" />
                </div>
                <input
                  type={showPassword ? "text" : "password"}
                  className={`input input-bordered w-full pl-10 rounded-md text-theme-400 p-2`}
                  placeholder="••••••••"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center text-theme-400"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? (
                    <EyeOff className="h-5 w-5 text-base-content/40" />
                  ) : (
                    <Eye className="h-5 w-5 text-base-content/40" />
                  )}
                </button>
              </div>
            </div>

            <button type="submit" className="flex justify-center btn btn-primary w-full rounded-lg bg-theme-400 p-2 hover:bg-theme-500" disabled={isLoading}>
              {isLoading ? (
                <>
                  <Loader2 className="h-6 w-6 animate-spin mr-2" />
                  Signing in...
                </>
              ) : (
                "Sign in"
              )}
            </button>
          </form>

          <div className="text-center">
            <p className="text-base-content/60">
              Don&apos;t have an account?{" "}
              <Link to="/signup" className="link link-primary underline hover:text-theme-400">
                Create account
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;