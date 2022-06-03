import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:tcl_mobile_app/bloc/films/films_bloc.dart';
import 'package:tcl_mobile_app/model/Films/film_response.dart';
import 'package:tcl_mobile_app/repository/films_repository/films_repository.dart';
import 'package:tcl_mobile_app/repository/films_repository/films_repository_impl.dart';
import 'package:tcl_mobile_app/ui/film_details.dart';
import 'package:tcl_mobile_app/ui/widgets/error_page.dart';
import 'package:tcl_mobile_app/ui/widgets/home_app_bar.dart';
import 'package:tcl_mobile_app/ui/widgets/skeleton_container.dart';
import '../repository/preferences_utils.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late FilmRepository filmRepository;
  String? avatar_url;
  String? token = "none";

  @override
  void initState() {
    PreferenceUtils.init();
    filmRepository = FilmRepositoryImpl();
    avatar_url = PreferenceUtils.getString("avatar");
    token = PreferenceUtils.getString("token");
    /*this._isLoading=true;
    Future.delayed(const Duration(seconds: 2),() {
      setState((){
        _isLoading = false;
      });
    });*/
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) {
        return FilmsBloc(filmRepository)..add(const FetchFilm());
      },
      child: Scaffold(
        body: _createSeeFilms(context),
        appBar: const HomeAppBar(),
      ),
    );
  }
}

Widget _createSeeFilms(BuildContext context) {
  return BlocBuilder<FilmsBloc, FilmsState>(
    builder: (context, state) {
      if (state is FilmsInitial) {
        return const Center(child: CircularProgressIndicator());
      } else if (state is FilmFetchError) {
        return ErrorPage(
          message: state.message,
          retry: () {
            context.watch<FilmsBloc>().add(const FetchFilm());
          },
        );
      } else if (state is FilmsFetched) {
        return _createPublicView(context, state.films);
      } else {
        return const Text('Not support');
      }
    },
  );
}

Widget _createPublicView(BuildContext context, List<Film> films) {
  final contentWidth = MediaQuery.of(context).size.width;
  final contentHeight = MediaQuery.of(context).size.height;
  PreferenceUtils.init();
  String? avatarUrl = PreferenceUtils.getString("avatar");
  String? token = PreferenceUtils.getString("token");
  String? nick = PreferenceUtils.getString("nick");
  return RefreshIndicator(
    onRefresh: () async {
      await Future.delayed(const Duration(seconds: 2),
          () => context.read<FilmsBloc>().add(const FetchFilm()));
    },
    triggerMode: RefreshIndicatorTriggerMode.onEdge,
    edgeOffset: 10,
    displacement: 50,
    strokeWidth: 3,
    color: const Color(0xFF263238),
    backgroundColor: const Color(0xFF1d1d1d),
    child: Scaffold(
      body: Container(
        color: const Color(0xFF263238),
        child: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              Container(
                  height: 120,
                  color: const Color(0xFF1d1d1d),
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 30.0, top: 20.0),
                        child: Row(
                          children: [
                            const Text("Hola",
                                style: TextStyle(
                                    color: Colors.white,
                                    fontSize: 20,
                                    fontWeight: FontWeight.w600)),
                            Padding(
                              padding: const EdgeInsets.only(left: 5.0),
                              child: SizedBox(
                                width: 100,
                                child: Text(nick!,
                                    style: const TextStyle(
                                        overflow: TextOverflow.ellipsis,
                                        color: Colors.white,
                                        fontSize: 20,
                                        fontWeight: FontWeight.w300)),
                              ),
                            ),
                            Padding(
                              padding: const EdgeInsets.only(left: 70.0),
                              child: ClipRRect(
                                borderRadius: BorderRadius.circular(50),
                                child: CachedNetworkImage(
                                  placeholder: (context, url) => const Center(
                                      child: SkeletonContainer.imageItem(
                                          width: 80.0,
                                          height: 80.0,
                                          radius: 50.0)),
                                  imageUrl: avatarUrl!,
                                  width: 80,
                                  height: 80,
                                  fit: BoxFit.cover,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  )),
              Container(
                color: const Color(0xFF263238),
                child: Padding(
                  padding:
                      const EdgeInsets.only(top: 15.0, left: 30, bottom: 10),
                  child: Row(
                    children: [
                      Text(
                        'En ',
                        style: TextStyle(
                            color: Colors.white.withOpacity(1),
                            fontWeight: FontWeight.w400,
                            fontSize: 24),
                      ),
                      Text(
                        'cartelera ',
                        style: TextStyle(
                            color: Colors.white.withOpacity(1),
                            fontWeight: FontWeight.w800,
                            fontSize: 25),
                      ),
                    ],
                  ),
                ),
              ),
              Container(
                color: const Color(0xFF263238),
                height: contentHeight * 0.4,
                child: Padding(
                  padding: const EdgeInsets.all(5.0),
                  child: ListView.builder(
                    padding: const EdgeInsets.all(10.0),
                    itemBuilder: (BuildContext context, int index) {
                      return _createPublicViewItem(context, films[index]);
                    },
                    scrollDirection: Axis.horizontal,
                    itemCount: films.length,
                  ),
                ),
              ),
              Container(
                color: const Color(0xFF263238),
                child: Padding(
                  padding: const EdgeInsets.only(top: 30.0, left: 0, bottom: 5),
                  child: Text(
                    'Promo',
                    style: TextStyle(
                        color: Colors.white.withOpacity(1),
                        fontWeight: FontWeight.w600,
                        fontSize: 23),
                  ),
                ),
              ),
              Container(
                height: 140,
                margin: const EdgeInsets.symmetric(
                    horizontal: 15, vertical: 10), // add margin
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(25),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.5),
                      spreadRadius: 4,
                      blurRadius: 7,
                      offset: const Offset(1, 1), // changes position of shadow
                    ),
                  ],
                ),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(25),
                  child: Image.asset(
                    "assets/img/promo/Promo1.jpg",
                    width: contentWidth,
                    height: contentHeight,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              Container(
                height: 140,
                margin: const EdgeInsets.symmetric(
                    horizontal: 15, vertical: 10), // add margin
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(25),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.5),
                      spreadRadius: 4,
                      blurRadius: 7,
                      offset: const Offset(1, 1), // changes position of shadow
                    ),
                  ],
                ),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(25),
                  child: Image.asset(
                    "assets/img/promo/Promo2.png",
                    width: contentWidth,
                    height: contentHeight,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
              Container(
                height: 140,
                margin: const EdgeInsets.symmetric(
                    horizontal: 15, vertical: 10), // add margin
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(25),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.5),
                      spreadRadius: 4,
                      blurRadius: 7,
                      offset: const Offset(1, 1), // changes position of shadow
                    ),
                  ],
                ),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(25),
                  child: Image.asset(
                    "assets/img/promo/Promo3.jpg",
                    width: contentWidth,
                    height: contentHeight,
                    fit: BoxFit.cover,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    ),
  );
}

Widget _createPublicViewItem(
  BuildContext context,
  Film film,
) {
  final contentWidth = MediaQuery.of(context).size.width - 180;
  final contentHeight = MediaQuery.of(context).size.height;

  String? token = PreferenceUtils.getString("token");

  return InkWell(
    onTap: () => Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => FilmDetails(filmUuid: film.uuid),
      ),
    ),
    child: Container(
      width: contentWidth,
      height: contentHeight,
      margin: const EdgeInsets.symmetric(horizontal: 15), // add margin
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(25),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.5),
            spreadRadius: 4,
            blurRadius: 7,
            offset: const Offset(1, 1), // changes position of shadow
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(25),
        child: CachedNetworkImage(
          placeholder: (context, url) => Center(
            child: SkeletonContainer.imageItem(
              width: contentWidth,
              height: contentHeight,
              radius: 25.0,
            ),
          ),
          imageUrl: film.poster,
          width: contentWidth,
          height: contentHeight,
          fit: BoxFit.cover,
        ),
      ),
    ),
  );
}
