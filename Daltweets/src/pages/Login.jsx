import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailError, setEmailError] = useState(false);
  const [passwordError, setPasswordError] = useState(false);

  const [emailErrorMessage, setEmailErrorMessage] = useState(
    "Username is requied",
  );
  const [passwordErrorMessage, setPasswordErrorMessage] = useState(
    "Password is required",
  );

  let navigate = useNavigate();

  const handleLogin = () => {
    if (email === "") {
      setEmailError(true);
      setEmailErrorMessage("Username is required");
    } else {
      setEmailError(false);
    }

    if (password === "") {
      setPasswordError(true);
      setPasswordErrorMessage("Password is required");
      return;
    }

    axios
      .post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/login/`, {
        username: email,
        password: password,
      })
      .then((response) => {
        setPasswordError(false);
        localStorage.setItem("user", JSON.stringify(response.data));
        const user = JSON.parse(localStorage.getItem("user"));
        user.status == 'PENDING'?  navigate("/pending-approval"):navigate("/home");
      })
      .catch((error) => {
        console.log(error);
        setPasswordError(true);
        setPasswordErrorMessage("Incorrect Username or Password");
        localStorage.setItem("user", null);
        console.log(JSON.parse(localStorage.getItem("user")) ?? "user is null");
      });
  };
  return (
    <>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <img src="favicon.ico" alt="" width="200" className="block mx-auto max-w-full h-auto"/>
        <h1 className="text-center text-4xl font-bold leading-9 tracking-tight text-gray-900">DalTweets</h1>
        <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Login
        </h2>
        <br />
        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6">
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Email
              </label>
              <div>
                <input
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
                {emailError && emailErrorMessage}
              </div>
            </div>
            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="password"
                  className="block text-sm font-medium leading-6 text-gray-900"
                >
                  Password
                </label>
                <div className="text-sm">
                  <a
                    href="/forgot-password"
                    className="font-semibold text-black-600 hover:text-yellow-500"
                  >
                    Forgot password?
                  </a>
                </div>
              </div>
              <div className="mt-2">
                <input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
                {passwordError && <div>{passwordErrorMessage}</div>}
              </div>
            </div>
          </form>

          <div>
            <button
              type="submit"
              onClick={handleLogin}
              className="mt-10 flex w-full justify-center rounded-md bg-black px-3 py-1.5 text-sm font-semibold leading-6 text-yellow-500 shadow-sm hover:bg-yellow-500 hover:text-black focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Login
            </button>
          </div>
        </div>
        <p className="mt-10 text-center text-sm text-gray-500">
          <a
            href="/signup"
            className="font-semibold leading-6 text-black-600 hover:text-yellow-500"
          >
            Sign Up
          </a>
        </p>
      </div>
    </>
  );
}

export default Login;
