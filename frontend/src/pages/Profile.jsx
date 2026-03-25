import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { postAPI } from "../services/UserService";
import PostCard from "../components/PostCard";
import { AiOutlineArrowLeft } from "react-icons/ai";

export default function ProfilePage() {
  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const storedUser = localStorage.getItem("user");
  const user = storedUser ? JSON.parse(storedUser) : null;

  useEffect(() => {
    if (!user || !user.userId) {
      navigate("/");
      return;
    }

    const fetchPosts = async () => {
      try {
        setLoading(true);
        const response = await postAPI.getUserPosts(user.userId);
        console.log('Posts:', response.data);
        setPosts(response.data.content);
        setError(null);
      } catch (err) {
        console.error('Error:', err);
        setError('Post yüklenemedi');
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, [user?.userId, navigate]);

  if (!user) return null; 

  return (
    <div className="flex min-h-screen max-w-370 mx-auto border-x border-gray-700 bg-gray-900 text-white">
      <main className="flex-1">
        {/* HEADER */}
        <header className="p-4 border-b border-b-blue-100-700 sticky top-0 bg-gray-900/80 backdrop-blur-md flex items-center gap-4 z-10">
          <button onClick={() => navigate(-1)} className="p-2 hover:bg-gray-800 rounded-full">
            <AiOutlineArrowLeft size={20} />
          </button>
          <div>
            <h2 className="text-xl font-bold">Profile</h2>
            <p className="text-xs text-gray-500">{posts.length} Posts</p>
          </div>
        </header>

        {/* INFO SECTION - Genişletilmiş ve Hakkımızda Alanı Eklenmiş */}
        <div className="py-12 px-8 border-b border-b-blue-100 from-gray-800 to-gray-900">
          <div className="flex flex-col items-center text-center">
            <div className="w-24 h-24 bg-blue-600 rounded-full flex items-center justify-center text-3xl font-bold mb-4 border-4 border-gray-700 shadow-xl">
              {user.email?.charAt(0).toUpperCase()}
            </div>
            <h1 className="text-3xl font-extrabold">{user.email}</h1>
            
            {/* Kulüp Açıklama Alanı (About) */}
            <div className="mt-6 max-w-lg">
              <p className="text-gray-300 text-sm leading-relaxed">
                Welcome to our club page! We share our latest events, 
                workshops, and community updates here. Stay tuned for more.
              </p>
            </div>
          </div>
        </div>

        {/* FEED - Postlar daha küçük ve ortalı */}
        <div className="p-4 flex flex-col items-center bg-gray-800">
          <div className="w-full max-w-7xl"> 
            {loading ? (
              <div className="p-10 text-center text-gray-500">Loading...</div>
            ) : error ? (
              <div className="p-10 text-center text-red-500 bg-red-900/10 rounded">{error}</div>
            ) : posts.length === 0 ? (
              <div className="p-10 text-center text-gray-500 italic">No posts yet</div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {posts.map((post) => (
                  <PostCard key={post.id} post={post} />
                ))}
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}