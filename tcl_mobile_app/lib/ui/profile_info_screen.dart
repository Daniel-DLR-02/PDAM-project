import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tcl_mobile_app/bloc/user/user_bloc.dart';
import 'package:tcl_mobile_app/model/User/user_response.dart';
import 'package:tcl_mobile_app/repository/user_repository/user_repository_impl.dart';
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
      body: Container(
        padding: const EdgeInsets.symmetric(horizontal: 15.0),
        child: Padding(
          padding: const EdgeInsets.only(top: 20.0),
          child: Column(children: [
            Row(
              children: [
                Padding(
                  padding: const EdgeInsets.only(top: 25, left: 5),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(100),
                    child: CachedNetworkImage(
                      placeholder: (context, url) => const Center(
                        child: CircularProgressIndicator(),
                      ),
                      imageUrl: user.avatar,
                      httpHeaders: {"Authorization": "Bearer " + token},
                      width: 80,
                      height: 80,
                      fit: BoxFit.cover,
                    ),
                  ),
                ),
              ],
            ),
            SizedBox(
              width: deviceWidth,
              child: Padding(
                padding:
                    const EdgeInsets.symmetric(horizontal: 5, vertical: 20),
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(user.nombre,
                          style: TextStyle(
                            color: Colors.black.withOpacity(.8),
                            fontWeight: FontWeight.bold,
                          )),
                      Padding(
                        padding: const EdgeInsets.only(top: 15.0),
                        child: Text("Email: " + user.email,
                            style: TextStyle(
                              color: Colors.black.withOpacity(.8),
                            )),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top: 10.0),
                        child: Text(
                            "Fecha de nacimiento: " + user.fechaDeNacimiento,
                            style: TextStyle(
                              color: Colors.black.withOpacity(.8),
                            )),
                      ),
                    ]),
              ),
            ),
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
              Container(
                alignment: Alignment.center,
                width: deviceWidth - 75,
                height: 35.0,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(4.0),
                  border: Border.all(
                    width: 2.0,
                    color: Colors.black,
                  ),
                ),
                child: const Text("Editar Perfil",
                    style: TextStyle(color: Colors.black)),
              ),
              const Icon(Icons.expand_more_outlined, color: Colors.black),
            ]),
            const SizedBox(height: 24.0),
            Divider(
              color: Colors.grey[800],
              thickness: 2.0,
            ),
            const SizedBox(height: 12.0),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: const [
                SizedBox(
                  width: 150.0,
                  child: Icon(Icons.grid_on_outlined, color: Colors.black),
                ),
              ],
            ),
          ]),
        ),
      ),
    );
  }
}
