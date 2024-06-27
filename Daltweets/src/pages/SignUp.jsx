import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios"; 

function SignUp() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [securityQuestion, setSecurityQuestion] = useState("");
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [error, setError] = useState("");

  const handleSignUp = () => {
    if (username === "" || password === "" || email === "" || securityQuestion === "" || securityAnswer === "") {
      setError("All fields are required");
      return;
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@dal\.ca$/;
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

    if (!emailRegex.test(email)) {
      setError("Email must be a valid @dal.ca address");
      return;
    }

    if (!passwordRegex.test(password)) {
      setError(
        "Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character"
      );
      return;
    }

    const signUpData = {
      user: {
        username: username,
        email: email,
      },
      login: {
        password: password,
        securityQuestion: securityQuestion,
        securityAnswer: securityAnswer,
      },
    };

    axios
      .post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/signup`,
        signUpData
      )
      .then((response) => {
        console.log(response);
        setError("");
        localStorage.setItem("user", JSON.stringify(response.data));
        JSON.parse(localStorage.getItem("user"));
        navigate("/home")
      })
      .catch((error) => {
        console.log(error);
        setError(error.response.data);
        localStorage.setItem("user", null);
        console.log(JSON.parse(localStorage.getItem("user")) ?? "user is null");
      });
  };

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
        Sign Up
      </h2>
      <br />
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form className="space-y-6">
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Username
            </label>
            <div>
              <input
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
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
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Password
            </label>
            <div className="mt-2">
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          <div>
            <label
              htmlFor="securityQuestion"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Security Question
            </label>
            <div className="mt-2">
              <select
                id="securityQuestion"
                value={securityQuestion}
                onChange={(e) => setSecurityQuestion(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              >
                <option value="">Select a security question</option>
                <option value="What is your mother's maiden name?">What is your mother's maiden name?</option>
                <option value="What was the name of your first pet?">What was the name of your first pet?</option>
                <option value="What was the name of your first school?">What was the name of your first school?</option>
                <option value="What is your favorite food?">What is your favorite food?</option>
              </select>
            </div>
          </div>
          <div>
            <label
              htmlFor="securityAnswer"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Security Answer
            </label>
            <div className="mt-2">
              <input
                id="securityAnswer"
                type="text"
                value={securityAnswer}
                onChange={(e) => setSecurityAnswer(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
        </form>

        {error && <div className="text-red-500 mt-4">{error}</div>}

        <div>
          <button
            type="submit"
            onClick={handleSignUp}
            className="mt-10 flex w-full justify-center rounded-md bg-black px-3 py-1.5 text-sm font-semibold leading-6 text-yellow-500 shadow-sm hover:bg-yellow-500 hover:text-black focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
          >
            Sign Up
          </button>
        </div>
      </div>
      <p className="mt-10 text-center text-sm text-gray-500">
        <a
          href="/login"
          className="font-semibold leading-6 text-black-600 hover:text-yellow-500"
        >
          Login
        </a>
      </p>
    </div>
  );
}

export default SignUp;
