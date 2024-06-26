import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Footer from "./commons/Footer";
import Login from "./components/Login";
import { createContext, useEffect, useReducer, useState } from "react";
import UserReducer from "./reducers/UserReducer";
import cookie from "react-cookies";
import SignUp from "./components/SignUp";
import AccountDetails from "./components/AccountDetails";
import APIs, { endpoints } from "./configs/APIs";
import ResponsiveAppBar from "./UI components/ResponsiveAppBar";
import Content from "./commons/Content";
import MyOutline from "./components/MyOutlines";
import Outlinecompilation from "./components/Outlinecompilation";
import StudentRegister from "./components/StudentRegister";
import StudentActive from "./components/StudentActive";
import ChatComponent from "./components/ChatComponents";
import OutlineDetailPage from "./components/OutlineDetailPage";
import { isLecturer, isStudent } from "./UserAuthorization/UserAuthoriation";
import DownloadedOutlines from "./components/DownloadedOutlines";

export const UserContext = createContext();
export const NonAdminUsersContext = createContext();
export const FacultyContext = createContext();
export const MajorContext = createContext();
export const GradeContext = createContext();
export const AcademicYearContext = createContext();
export const SubjectsContext = createContext();
export const LecturerContext = createContext();

const App = () => {
	const [faculties, setFaculties] = useState([]);
	const [major, setMajor] = useState([]);
	const [grade, setGrade] = useState([]);
	const [academicYear, setAcademicYear] = useState([]);
	const [subjects, setSubjects] = useState([]);
	const [lecturers, setLecturers] = useState([]);
	const [nonAdminUsers, setNonAdminUsers] = useState([]);

	const loadFaculties = async () => {
		try {
			let url = `${endpoints["getFaculties"]}`;
			let res = await APIs.get(url);

			setFaculties(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	const loadMajors = async () => {
		try {
			let url = `${endpoints["getMajors"]}`;
			let res = await APIs.get(url);

			setMajor(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	const loadGrades = async () => {
		try {
			let url = `${endpoints["getGrades"]}`;
			let res = await APIs.get(url);

			setGrade(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	const loadAcademicYears = async () => {
		try {
			let url = `${endpoints["getAcademicYears"]}`;
			let res = await APIs.get(url);

			setAcademicYear(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	const loadSubjects = async () => {
		try {
			let url = `${endpoints["getSubjetcs"]}`;
			let res = await APIs.get(url);

			setSubjects(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	const loadLecturers = async () => {
		try {
			let url = `${endpoints["getLecturers"]}`;
			let res = await APIs.get(url);

			setLecturers(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	const loadNonAdminUsers = async () => {
		try {
			let url = `${endpoints["getNonAdminUsers"]}`;
			let res = await APIs.get(url);

			setNonAdminUsers(res.data);
		} catch (ex) {
			console.error(ex);
		}
	};

	useEffect(() => {
		loadFaculties();
		loadMajors();
		loadGrades();
		loadAcademicYears();
		loadSubjects();
		loadLecturers();
		loadNonAdminUsers();
	}, []);

	const [user, dispatch] = useReducer(
		UserReducer,
		cookie.load("user") || null,
	);

	const requireLogin = (element) => {
		return user ? element : <Navigate to="/login" />;
	};

	return (
		<UserContext.Provider value={[user, dispatch]}>
			<FacultyContext.Provider value={faculties}>
				<MajorContext.Provider value={major}>
					<GradeContext.Provider value={grade}>
						<AcademicYearContext.Provider value={academicYear}>
							<SubjectsContext.Provider value={subjects}>
								<LecturerContext.Provider value={lecturers}>
									<NonAdminUsersContext.Provider
										value={nonAdminUsers}
									>
										<BrowserRouter>
											{/* <Header /> */}
											<ResponsiveAppBar />

											{/* <Container> */}
											<Routes>
												{user === null && (
													<>
														<Route
															path="/lecturer-signup"
															element={<SignUp />}
														/>
														<Route
															path="/student-register"
															element={
																<StudentRegister />
															}
														/>
														<Route
															path="/login"
															element={<Login />}
														/>
													</>
												)}

												<Route
													path="/account-details"
													element={requireLogin(
														<AccountDetails />,
													)}
												/>
												<Route
													path="/student-active"
													element={<StudentActive />}
												/>

												<Route
													path="/"
													element={<Content />}
												/>

												{isStudent(user) && (
													<Route
														path="/downloaded-outlines"
														element={requireLogin(
															<DownloadedOutlines />,
														)}
													/>
												)}

												<Route
													path="/detail-page/:outlineId"
													element={requireLogin(
														<OutlineDetailPage />,
													)}
												/>

												{isLecturer(user) && (
													<>
														<Route
															path="/my-outlines"
															element={requireLogin(
																<MyOutline status="ACCEPTED" />,
															)}
														/>
														<Route
															path="/my-workspace"
															element={requireLogin(
																<MyOutline status="HOLDING" />,
															)}
														/>
														<Route
															path="/outline-compiling"
															element={requireLogin(
																<Outlinecompilation />,
															)}
														/>
													</>
												)}
												<Route
													path="/chat-real-time"
													element={requireLogin(
														<ChatComponent />,
													)}
												/>
											</Routes>
											{/* </Container> */}
											<Footer />
										</BrowserRouter>
									</NonAdminUsersContext.Provider>
								</LecturerContext.Provider>
							</SubjectsContext.Provider>
						</AcademicYearContext.Provider>
					</GradeContext.Provider>
				</MajorContext.Provider>
			</FacultyContext.Provider>
		</UserContext.Provider>
	);
};

export default App;
