import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tcl_mobile_app/bloc/user/user_bloc.dart';
import 'package:tcl_mobile_app/constants.dart';
import 'package:tcl_mobile_app/model/User/user_response.dart';
import 'package:tcl_mobile_app/repository/user_repository/user_repository_impl.dart';
import 'package:tcl_mobile_app/ui/profile_edit_form.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';
import '../repository/preferences_utils.dart';
import '../repository/user_repository/user_repository.dart';

class ProfileInfoScreen extends StatefulWidget {
  const ProfileInfoScreen({Key? key}) : super(key: key);

  @override
  State<ProfileInfoScreen> createState() => _ProfileInfoScreenState();
}

class _ProfileInfoScreenState extends State<ProfileInfoScreen> {
  late UserRepository userRepository;
  late String token;

  @override
  void initState() {
    super.initState();
    PreferenceUtils.init();
    userRepository = UserRepositoryImpl();
    token = PreferenceUtils.getString("token")!;
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
        create: (context) {
          return UserBloc(userRepository)..add(const FetchUser());
        },
        child: _createBody(context));
  }

  _createBody(BuildContext context) {
    return BlocBuilder<UserBloc, UserState>(
      builder: (context, state) {
        if (state is UserInitial) {
          return const Center(child: CircularProgressIndicator());
        } else if (state is UserFetchError) {
          return ErrorPage(
            message: state.message,
            retry: () {
              context.watch<UserBloc>();
            },
          );
        } else if (state is UserFetched) {
          return _buildPage(context, state.user);
        } else {
          return const Text('Not support');
        }
      },
    );
  }

  void _showSnackbar(BuildContext context, String message) {
    final snackBar = SnackBar(
      content: Text(message),
    );
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }

  Widget _buildPage(BuildContext context, UserResponse user) {
    double deviceWidth = MediaQuery.of(context).size.width;
    return Scaffold(
      backgroundColor: const Color(0xFF263238),
      body: Column(children: [
        Container(
          height: 65,
          color: const Color(0xFF1d1d1d),
          child: Row(
            children: const [
              Padding(
                padding: EdgeInsets.only(top: 7.0, left: 20),
                child: Text(
                  "Información del usuario",
                  style: TextStyle(color: Colors.white, fontSize: 15.0),
                ),
              ),
            ],
          ),
        ),
        Column(
          children: [
            Container(
              color: const Color(0xFF1d1d1d),
              child: Padding(
                padding: const EdgeInsets.only(
                    top: 25.0, right: 20.0, left: 20.0, bottom: 30),
                child: Row(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(top: 25, left: 5),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(100),
                        child: CachedNetworkImage(
                          placeholder: (context, url) => const Center(
                            child: CircularProgressIndicator(),
                          ),
                          imageUrl: user.avatar != null ? user.avatar! : Constants.defaultUserImage,
                          httpHeaders: {"Authorization": "Bearer " + token},
                          width: 80,
                          height: 80,
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top: 30, left: 70),
                      child: Text(user.nombre,
                          style: TextStyle(
                            fontSize: 20,
                            color: Colors.white.withOpacity(.8),
                            fontWeight: FontWeight.bold,
                          )),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
        SizedBox(
          width: deviceWidth,
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 25, vertical: 20),
            child:
                Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              Container(
                decoration: BoxDecoration(
                    color: const Color(0xFF1d1d1d),
                    borderRadius: BorderRadius.circular(25)),
                child: Padding(
                  padding: const EdgeInsets.all(15.0),
                  child: Text("Nombre:     " + user.nombre,
                      style: TextStyle(
                        color: Colors.white.withOpacity(.8),
                      )),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 20.0),
                child: Container(
                  decoration: BoxDecoration(
                      color: const Color(0xFF1d1d1d),
                      borderRadius: BorderRadius.circular(25)),
                  child: Padding(
                    padding: const EdgeInsets.all(15.0),
                    child: Text("Email:     " + user.email,
                        style: TextStyle(
                          color: Colors.white.withOpacity(.8),
                        )),
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(top: 20.0),
                child: Container(
                  decoration: BoxDecoration(
                      color: const Color(0xFF1d1d1d),
                      borderRadius: BorderRadius.circular(25)),
                  child: Padding(
                    padding: const EdgeInsets.all(15.0),
                    child: Text(
                        "Fecha de nacimiento:     " + user.fechaDeNacimiento,
                        style: TextStyle(
                          color: Colors.white.withOpacity(.8),
                        )),
                  ),
                ),
              ),
            ]),
          ),
        ),
        Padding(
          padding: const EdgeInsets.only(top: 100.0, left: 30),
          child: InkWell(
            onTap: () => Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => ProfileEditForm(
                  nombre: user.nombre,
                  nick: user.nick,
                  email: user.email,
                  avatar: user.avatar != null ? user.avatar! : Constants.defaultUserImage,
                  fechaNacimiento: user.fechaDeNacimiento,
                ),
              ),
            ),
            child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    alignment: Alignment.center,
                    width: deviceWidth - 75,
                    height: 35.0,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(4.0),
                      color: const Color(0xFF1d1d1d),
                      border: Border.all(
                        width: 2.0,
                        color: Colors.black,
                      ),
                    ),
                    child: const Text("Editar Perfil",
                        style: TextStyle(color: Colors.white)),
                  ),
                ]),
          ),
        ),
      ]),
    );
  }
}
