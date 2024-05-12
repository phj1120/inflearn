import type { CourseWithPath } from '~/types/course';

interface CourseReturn {
  course: Maybe<CourseWithPath>;
  prevCourse: Maybe<CourseWithPath>;
  nextCourse: Maybe<CourseWithPath>;
}

export const useCourse = (courseSlug: string): CourseReturn => {
  const { courses } = useCourses();
  const idx = courses.findIndex((item) => item.courseSlug === courseSlug);
  const course = courses[idx];
  const prevCourse = idx <= 0 ? null : courses[idx - 1];
  const nextCourse = idx >= courses.length - 1 ? null : courses[idx + 1];

  return { course, prevCourse, nextCourse };
};
